package com.xiaojia.daniujia.domain.resp;

import java.util.List;

/**
 * Created by Administrator on 2016/5/24.
 */
public class EducationVo {

    /**
     * educations : [{"eduId":22,"name":"熏鸡继续继续继续加","beginTime":946656000,"endTime":820425600,"degree":0},{"eduId":23,"name":"恶女if","beginTime":1199116800,"endTime":1356969600,"degree":0}]
     * returncode : SUCCESS
     * returnmsg : 数据返回成功
     */

    private String returncode;
    private String returnmsg;
    /**
     * eduId : 22
     * name : 熏鸡继续继续继续加
     * beginTime : 946656000
     * endTime : 820425600
     * degree : 0
     */

    private List<EducationsEntity> educations;

    public void setReturncode(String returncode) {
        this.returncode = returncode;
    }

    public void setReturnmsg(String returnmsg) {
        this.returnmsg = returnmsg;
    }

    public void setEducations(List<EducationsEntity> educations) {
        this.educations = educations;
    }

    public String getReturncode() {
        return returncode;
    }

    public String getReturnmsg() {
        return returnmsg;
    }

    public List<EducationsEntity> getEducations() {
        return educations;
    }

    public static class EducationsEntity {
        private int eduId;
        private String name;
        private long beginTime;
        private long endTime;
        private int degree;

        public void setEduId(int eduId) {
            this.eduId = eduId;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setBeginTime(int beginTime) {
            this.beginTime = beginTime;
        }

        public void setEndTime(int endTime) {
            this.endTime = endTime;
        }

        public void setDegree(int degree) {
            this.degree = degree;
        }

        public int getEduId() {
            return eduId;
        }

        public String getName() {
            return name;
        }

        public void setBeginTime(long beginTime) {
            this.beginTime = beginTime;
        }

        public long getBeginTime() {
            return beginTime;
        }

        public void setEndTime(long endTime) {
            this.endTime = endTime;
        }

        public long getEndTime() {
            return endTime;
        }

        public int getDegree() {
            return degree;
        }
    }
}
