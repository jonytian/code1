/**
 * @author SargerasWang
 */
package com.legaoyi.common.excel.util;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Map;

/**
 * 测试导入Excel 97/2003
 */
public class TestImportExcel {


  public void importXls() throws FileNotFoundException {
    File f=new File("src/test/resources/test.xls");
    InputStream inputStream= new FileInputStream(f);
    
    ExcelLogs logs =new ExcelLogs();
    Collection<Map> importExcel = ExcelUtil.importExcel(Map.class, inputStream, "yyyy/MM/dd HH:mm:ss", logs , 0);
    
    for(Map m : importExcel){
      System.out.println(m);
    }
  }

  public static  void importXlsx() throws FileNotFoundException {
    File f=new File("C:\\Users\\78772\\Desktop\\设备批量导入模板\\设备批量导入模板.xlsx");
    InputStream inputStream= new FileInputStream(f);

    ExcelLogs logs =new ExcelLogs();
    Collection<Map> importExcel = ExcelUtil.importExcel(Map.class, inputStream, "yyyy/MM/dd HH:mm:ss", logs , 0);

    for(Map m : importExcel){
      System.out.println(m);
    }
  }

  public static void main(String[] arg) {
      try {
        importXlsx();
    } catch (FileNotFoundException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }
  }
}
