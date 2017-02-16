/*
 * Copyright 2013 Niek Haarman
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.xiaojia.daniujia.ui.animadapter;

import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;

import junit.framework.Assert;

/**
 * A BaseAdapterDecorator class which applies multiple Animators at once to
 * views when they are first shown. The Animators applied include the animations
 * specified in getAnimators(ViewGroup, View), plus an alpha transition.
 */
public abstract class AnimationAdapter extends BaseAdapterDecorator {

	protected static final long DEFAULTANIMATIONDELAYMILLIS = 100;
	protected static final long DEFAULTANIMATIONDURATIONMILLIS = 550;

	private static final long INITIALDELAYMILLIS = 50;

	private SparseArray<AnimationInfo> mAnimators;
	private long mAnimationStartMillis;
	private int mLastAnimatedPosition;

	private boolean mHasParentAnimationAdapter;

	public AnimationAdapter(BaseAdapter baseAdapter) {
		super(baseAdapter);
		mAnimators = new SparseArray<>();

		mAnimationStartMillis = -1;
		mLastAnimatedPosition = -1;

		// 这里的baseAdapter是MyListActivity的createListAdapter中创建的，所以这个条件不会满足
		if (baseAdapter instanceof AnimationAdapter) {
			((AnimationAdapter) baseAdapter).setHasParentAnimationAdapter(true);
		}
	}

	/**
	 * Call this method to reset animation status on all views. The next time
	 * notifyDataSetChanged() is called on the base adapter, all views will
	 * animate again.
	 */
	public void reset() {
		mAnimators.clear();
		mLastAnimatedPosition = -1;
		mAnimationStartMillis = -1;

		if (getDecoratedBaseAdapter() instanceof AnimationAdapter) {
			((AnimationAdapter) getDecoratedBaseAdapter()).reset();
		}
	}

	@Override
	public final View getView(int position, View convertView, ViewGroup parent) {
		boolean alreadyStarted = false;
		if (!mHasParentAnimationAdapter) {
			Assert.assertNotNull(
					"Call setListView() on this AnimationAdapter before setAdapter()!",
					getListView());

			if (convertView != null) {
				int hashCode = convertView.hashCode();
				AnimationInfo animationInfo = mAnimators.get(hashCode);
				if (animationInfo != null) {
					if (animationInfo.position != position) {
						animationInfo.animator.end();
						mAnimators.remove(hashCode);
					} else {
						alreadyStarted = true;
					}
				}
			}
		}

		View itemView = super.getView(position, convertView, parent);

		if (!mHasParentAnimationAdapter && !alreadyStarted) {
			animateViewIfNecessary(position, itemView, parent);
		}
		return itemView;
	}

	private void animateViewIfNecessary(int position, View view, ViewGroup parent) {
		if (position > mLastAnimatedPosition && !mHasParentAnimationAdapter) {
			mLastAnimatedPosition = position;
			animateView(position, parent, view);
		}
	}

	private void animateView(int position, ViewGroup parent, View view) {
		if (mAnimationStartMillis == -1) {
			mAnimationStartMillis = System.currentTimeMillis();
		}

		hideView(view);

		Animator[] childAnimators;
		if (mDecoratedBaseAdapter instanceof AnimationAdapter) {
			childAnimators = ((AnimationAdapter) mDecoratedBaseAdapter).getAnimators(parent, view);
		} else {
			childAnimators = new Animator[0];
		}
		// getAnimators抽象方法，子类中去实现具体动画类型
		Animator[] animators = getAnimators(parent, view);
		Animator alphaAnimator = ObjectAnimator.ofFloat(view, "alpha", 0, 1);

		AnimatorSet set = new AnimatorSet();
		set.playTogether(concatAnimators(childAnimators, animators, alphaAnimator));
		set.setStartDelay(calculateAnimationDelay());
		set.setDuration(getAnimationDurationMillis());
		set.start();

		mAnimators.put(view.hashCode(), new AnimationInfo(position, set));
	}

	private void hideView(View view) {
		ObjectAnimator animator = ObjectAnimator.ofFloat(view, "alpha", 0);
		AnimatorSet set = new AnimatorSet();
		set.play(animator);
		set.setDuration(0);
		set.start();
	}

	private Animator[] concatAnimators(Animator[] childAnimators, Animator[] animators,
			Animator alphaAnimator) {
		Animator[] allAnimators = new Animator[childAnimators.length + animators.length + 1];
		int i;

		for (i = 0; i < animators.length; ++i) {
			allAnimators[i] = animators[i];
		}

		for (int j = 0; j < childAnimators.length; ++j) {
			allAnimators[i] = childAnimators[j];
			++i;
		}

		allAnimators[allAnimators.length - 1] = alphaAnimator;
		return allAnimators;
	}

	private long calculateAnimationDelay() {
		long delay;
		int numberOfItems = getListView().getLastVisiblePosition()
				- getListView().getFirstVisiblePosition();
		// 加载超出一个屏幕了,这个时候
		if (numberOfItems + 1 < mLastAnimatedPosition) {
			delay = getAnimationDelayMillis();
			Log.d("zptest","if delay: " + delay);
		} else {
			long delaySinceStart = mLastAnimatedPosition * getAnimationDelayMillis();
			delay = mAnimationStartMillis + INITIALDELAYMILLIS + delaySinceStart
					- System.currentTimeMillis();
			Log.d("zptest","else delay: " + delay);
		}

		return Math.max(0, delay);
	}

	/**
	 * Set whether this AnimationAdapter is encapsulated by another
	 * AnimationAdapter. When this is set to true, this AnimationAdapter does
	 * not apply any animations to the views. Should not be set explicitly, the
	 * AnimationAdapter class manages this by itself.
	 */
	public void setHasParentAnimationAdapter(boolean hasParentAnimationAdapter) {
		mHasParentAnimationAdapter = hasParentAnimationAdapter;
	}

	/**
	 * Get the delay in milliseconds before an animation of a view should start.
	 */
	protected abstract long getAnimationDelayMillis();

	/**
	 * Get the duration of the animation in milliseconds.
	 */
	protected abstract long getAnimationDurationMillis();

	/**
	 * Get the Animators to apply to the views. In addition to the returned
	 * Animators, an alpha transition will be applied to the view.
	 * 
	 * @param parent
	 *            The parent of the view
	 * @param view
	 *            The view that will be animated, as retrieved by getView()
	 */
	public abstract Animator[] getAnimators(ViewGroup parent, View view);

	private class AnimationInfo {
		public int position;
		public Animator animator;

		public AnimationInfo(int position, Animator animator) {
			this.position = position;
			this.animator = animator;
		}
	}
}