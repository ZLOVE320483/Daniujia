package com.xiaojia.daniujia.domain.resp;

import java.util.List;

/**
 * Created by Administrator on 2016/5/11.
 */
public class IndustryAndFunctionVo {
    public List<Novation> novations;

    public List<Novation> getNovations() {
        return novations;
    }

    public void setNovations(List<Novation> novations) {
        this.novations = novations;
    }

    public static class Novation{
        public int novationId;
        public String name;
        public List<Function> functions;

        public int getNovationId() {
            return novationId;
        }

        public void setNovationId(int novationId) {
            this.novationId = novationId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<Function> getFunctions() {
            return functions;
        }

        public void setFunctions(List<Function> functions) {
            this.functions = functions;
        }

        public static class Function{
            public int functionId;
            public String name;
            public int novationId;

            public int getFunctionId() {
                return functionId;
            }

            public void setFunctionId(int functionId) {
                this.functionId = functionId;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public int getNovationId() {
                return novationId;
            }

            public void setNovationId(int novationId) {
                this.novationId = novationId;
            }
        }
    }
}
