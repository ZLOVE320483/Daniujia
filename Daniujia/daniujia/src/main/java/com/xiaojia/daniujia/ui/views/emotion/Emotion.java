package com.xiaojia.daniujia.ui.views.emotion;

import com.xiaojia.daniujia.utils.HashCodeUtil;

/**
 * Created by ZLOVE on 2016/4/11.
 */
public class Emotion implements Comparable<Emotion> {

    private String face;

    private String desc;

    private int res;

    public Emotion(String face, String desc, int res) {
        this.face = face;
        this.desc = desc;
        this.res = res;
    }

    /**
     * @return the face
     */
    public String getFace() {
        return face;
    }

    /**
     * @param face the face to set
     */
    public void setFace(String face) {
        this.face = face;
    }

    /**
     * @return the tag
     */
    public String getDesc() {
        return desc;
    }

    /**
     * @param desc the tag to set
     */
    public void setDesc(String desc) {
        this.desc = desc;
    }

    /**
     * @return the res
     */
    public int getRes() {
        return res;
    }

    /**
     * @param res the res to set
     */
    public void setRes(int res) {
        this.res = res;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int compareTo(Emotion another) {
        try {
            return Integer.valueOf(face).compareTo(Integer.valueOf(another.face));
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return face.compareTo(another.face);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object o) {
        if (o instanceof Emotion && desc != null) {
            return desc.equals(((Emotion) o).getDesc());
        } else {
            return super.equals(o);
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        int seed = HashCodeUtil.SEED;
        return HashCodeUtil.hash(seed, desc);
    }
}
