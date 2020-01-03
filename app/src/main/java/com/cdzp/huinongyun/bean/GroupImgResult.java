package com.cdzp.huinongyun.bean;

import java.util.List;

/**
 * 虫情监控图片
 */
public class GroupImgResult {

    /**
     * success : true
     * data :
     * Msg : 获取成功!
     */

    private boolean success;
    private String Msg;
    private List<List<DataBean>> data;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMsg() {
        return Msg;
    }

    public void setMsg(String Msg) {
        this.Msg = Msg;
    }

    public List<List<DataBean>> getData() {
        return data;
    }

    public void setData(List<List<DataBean>> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * FileName : 2017-09-30 17-39-01.jpg
         * ShowName : 2017-09-30 17:43
         * FilePath : /GroupPic/201709281001/2017-09-30 17-39-01.jpg
         */

        private String FileName;
        private String ShowName;
        private String FilePath;

        public String getFileName() {
            return FileName;
        }

        public void setFileName(String FileName) {
            this.FileName = FileName;
        }

        public String getShowName() {
            return ShowName;
        }

        public void setShowName(String ShowName) {
            this.ShowName = ShowName;
        }

        public String getFilePath() {
            return FilePath;
        }

        public void setFilePath(String FilePath) {
            this.FilePath = FilePath;
        }
    }
}
