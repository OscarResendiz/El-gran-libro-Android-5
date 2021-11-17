package com.example.oscar.fileexplorer2;

public class Archivo {
    private String Path;
    private String Name;
    private boolean isDir;
    public Archivo(String Name,String Path,boolean dir){
        this.Name = Name;
        this.Path = Path;
        this.isDir = dir;
    }
    public void setName(String Name) {
        this.Name = Name;
    }
    public void setPath(String Path){
        this.Path = Path;
    }
    public void setIsDir(boolean dir){
        this.isDir = dir;
    }
    public String getName() {
        return this.Name;
    }
    public String getPath() {
        return this.Path;
    }
    public boolean isDirectory() {
        return this.isDir;
    }
}
