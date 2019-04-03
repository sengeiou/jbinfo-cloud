package cn.jbinfo.tools.file.upload;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 文件存储接口
 *
 * @author xiaobin
 */
public interface Repository {

    String storeByExt(String ext, InputStream in) throws IOException;

    String storeByName(String name, InputStream in) throws IOException;

    boolean retrieve(String name, OutputStream out);
}
