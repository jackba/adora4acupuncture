package efan.zz.data.sql;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class AcupointSQLBuilder
{
  private static final String FILE_NAME = "aa_data_acupoint";
  private static final String INPUT_FILE = SQLBuilder.DATA_DIR_INPUT + FILE_NAME;
  private static final String OUTPUT_FILE = SQLBuilder.DATA_DIR_OUTPUT + "db_init.sql";
  
  private static final int COLUMN_NUM = 10;
  private static final String SQL_INIT0 = "select '";
  private static final String SQL_INIT = "union select '";
  
  public static void build() throws IOException
  {
    BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(OUTPUT_FILE, true), SQLBuilder.ENCODING));
    boolean isFirstRow = true;
    String initSQL = SQL_INIT0;

    for (int n=1; n<=14; n++)
    {
      BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(INPUT_FILE + n  + ".txt"), SQLBuilder.DECODING));
      String row = null;
      while ((row = in.readLine()) != null)
      {
        if (row.trim().length() == 0)
          continue;
        
        while (row.split("\"", -1).length % 2 == 0)
        {
          row += "\n" + in.readLine();
        }
        
        row = row.replaceAll("\"", "");
        
        String[] columns = row.split("\t", COLUMN_NUM);
  
        out.append(initSQL);
        for (int i=0; i<COLUMN_NUM-3; i++)
        {
          out.append(columns[i].trim()).append("', '");
        }
        out.append("<<WHO>>\n").append(columns[COLUMN_NUM-3].trim());
        out.append("\n\n<<acupuncture.com>>\n").append(columns[COLUMN_NUM-2].trim());
        out.append("\n\n<<rootdown.us>>\n").append(columns[COLUMN_NUM-1].trim());
        out.append("'");
        
        out.newLine();
        
        if (isFirstRow)
        {
          isFirstRow = false;
          initSQL = SQL_INIT;
        }
      }
  
      in.close();
    }

    out.append(";");
    out.close();
  }
}
