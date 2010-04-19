package efan.zz.data.sql;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class SQLBuilder
{
  public static final String VERSION = "1_1_0";
  
  public static final String DATA_DIR_BASE = "./data/";
  public static final String DATA_DIR_INPUT = DATA_DIR_BASE + "tmp/";
  public static final String DATA_DIR_OUTPUT = "./res/raw/";

  public static final String DATA_FILE_INIT = "db_init.sql";
  public static final String DATA_FILE_UPDATE = "db_update_" + VERSION + ".sql";
  public static final String TARGET_FILE = DATA_DIR_OUTPUT + DATA_FILE_INIT;
  public static final String TARGET_FILE_UPDATE = DATA_DIR_OUTPUT + DATA_FILE_UPDATE;
  
  public static final String DECODING = "UTF-16";
  public static final String ENCODING = "UTF-8";
  
  public static void main(String[] args) throws IOException
  {
    copy(DATA_DIR_BASE + DATA_FILE_INIT, TARGET_FILE);
    copy(DATA_DIR_BASE + DATA_FILE_UPDATE, TARGET_FILE_UPDATE);

    AcupointSQLBuilder.build();
  }
  
  private static void copy(String sourceFile, String targetFile) throws IOException
  {
    BufferedReader source = new BufferedReader(new FileReader(sourceFile));
    BufferedWriter target = new BufferedWriter(new FileWriter(targetFile, false));
    String oneLine = null;
    while ((oneLine = source.readLine()) != null)
    {
      target.write(oneLine);
      target.newLine();
    }
    target.close();
    source.close();
  }

}
