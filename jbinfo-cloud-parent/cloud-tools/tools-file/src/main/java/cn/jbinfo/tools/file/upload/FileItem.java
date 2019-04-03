package cn.jbinfo.tools.file.upload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author xiaobin
 * @create 2017-10-19 下午2:54
 **/
@Getter
@Setter
@AllArgsConstructor
public class FileItem {

    private String contentType;

    private File file;

    public String getName(){
        return file.getName();
    }

    public long getSize(){
        return file.length();
    }

    public InputStream getInputStream() throws IOException {
        return FileUtils.openInputStream(file);
    }

    public void transferTo(File dest) throws IOException {
        FileUtils.copyFile(file,dest);
    }
}
