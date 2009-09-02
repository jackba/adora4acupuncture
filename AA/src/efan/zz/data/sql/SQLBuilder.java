package efan.zz.data.sql;

import java.io.IOException;

public class SQLBuilder
{
  public static final String DATA_DIR_INPUT = "./assets/";
  public static final String DATA_DIR_OUTPUT = "./res/raw/";
  
  public static final String DECODING = "UTF-16";
  public static final String ENCODING = "UTF-8";
  
  public static void main(String[] args) throws IOException
  {
    AcupointSQLBuilder.build();
  }
}
