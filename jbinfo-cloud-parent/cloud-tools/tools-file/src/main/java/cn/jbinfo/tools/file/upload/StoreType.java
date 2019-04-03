package cn.jbinfo.tools.file.upload;

/**
 * 文件存储类型
 *
 * @author xiaobin
 */
public enum StoreType {

    /**
     * 返回项目路径
     */
    FOLDER {
        @Override
        public int getValue() {
            return 1;
        }

        @Override
        public String getName() {
            return "Folder";
        }
    },

    /**
     * 返回系统路径,本系统默认
     */
    SYSTEM {
        @Override
        public int getValue() {
            return 2;
        }

        @Override
        public String getName() {
            return "System";
        }

    },

    /**
     * FileNet存储
     */
    FILE_NET {
        @Override
        public int getValue() {
            return 3;
        }

        @Override
        public String getName() {
            return "FileNet";
        }

    },
    OSS {
        @Override
        public int getValue() {
            return 4;
        }

        @Override
        public String getName() {
            return "Oss";
        }
    };

    public abstract int getValue();

    public abstract String getName();
}