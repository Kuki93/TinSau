/**
  * Copyright 2018 bejson.com 
  */
package com.example.helpme.mvpandroid.entity.image;
import java.util.List;

/**
 * Auto-generated: 2018-02-13 14:56:6
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class Exif {

    private List<摘要> 摘要;
    private List<File> File;
    private List<IFD0> IFD0;
    private List<ExifIFD> ExifIFD;
    private List<IFD1> IFD1;

    public void set摘要(List<摘要> 摘要) {
         this.摘要 = 摘要;
     }
     public List<摘要> get摘要() {
         return 摘要;
     }

    public void setFile(List<File> File) {
         this.File = File;
     }
     public List<File> getFile() {
         return File;
     }

    public void setIFD0(List<IFD0> IFD0) {
         this.IFD0 = IFD0;
     }
     public List<IFD0> getIFD0() {
         return IFD0;
     }

    public void setExifIFD(List<ExifIFD> ExifIFD) {
         this.ExifIFD = ExifIFD;
     }
     public List<ExifIFD> getExifIFD() {
         return ExifIFD;
     }

}