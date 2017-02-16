package com.xiaojia.daniujia.domain.resp;


import java.util.List;

/**
 * Created by Administrator on 2016/5/16.
 */
public class NovationByTopicVoExtends{

    /**
     * pageNum : 1
     * perPageNum : 10
     * totalNum : 0
     * filterNovations : [{"name":"全部","code":"0000"}]
     * consultants : []
     * returncode : SUCCESS
     * returnmsg : 数据返回成功
     */

    private String pageNum;
    private int perPageNum;
    private String totalNum;

    @Override
    public String toString() {
        return "NovationByTopicVoExtends{" +
                "pageNum='" + pageNum + '\'' +
                ", perPageNum=" + perPageNum +
                ", totalNum='" + totalNum + '\'' +
                ", filterJobFuncs=" + filterJobFuncs +
                ", consultants=" + consultants +
                '}';
    }

    /**
     * name : 全部
     * code : 0000
     */
    private List<FilterJobFunction> filterJobFuncs;
    private List<ConsultEntity> consultants;

    public void setPageNum(String pageNum) {
        this.pageNum = pageNum;
    }

    public void setPerPageNum(int perPageNum) {
        this.perPageNum = perPageNum;
    }

    public void setTotalNum(String totalNum) {
        this.totalNum = totalNum;
    }


    public void setFilterJobFuncs(List<FilterJobFunction> filterNovations) {
        this.filterJobFuncs = filterNovations;
    }

    public void setConsultants(List<ConsultEntity> consultants) {
        this.consultants = consultants;
    }

    public String getPageNum() {
        return pageNum;
    }

    public int getPerPageNum() {
        return perPageNum;
    }

    public String getTotalNum() {
        return totalNum;
    }

    public List<FilterJobFunction> getFilterJobFuncs() {
        return filterJobFuncs;
    }

    public List<ConsultEntity> getConsultants() {
        return consultants;
    }

    public static class FilterJobFunction {

        private String name;
        private String jobFuncCode;

        public String getJobFuncCode() {
            return jobFuncCode;
        }

        public void setJobFuncCode(String jobFuncCode) {
            this.jobFuncCode = jobFuncCode;
        }

        public void setName(String name) {
            this.name = name;
        }



        public String getName() {
            return name;
        }

        @Override
        public String toString() {
            return "FilterJobFunction{" +
                    "name='" + name + '\'' +
                    ", jobFunCode='" + jobFuncCode + '\'' +
                    '}';
        }
    }
}
