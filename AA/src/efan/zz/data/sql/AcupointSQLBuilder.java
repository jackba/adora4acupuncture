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
  private static final String FILE_NAME = "aa_data";
  private static final String INPUT_FILE = SQLBuilder.DATA_DIR_INPUT + FILE_NAME;
  private static final String OUTPUT_FILE = SQLBuilder.TARGET_FILE;
  
  private static final int BASE_COLUMN_NUM = 7;
  private static final int COLUMN_NUM = 11;
  private static final int CHANNEL_NUM = 14;

  private static final String SQL_INIT0 = "select '";
  private static final String SQL_INIT = "union select '";

  private static final String SQL_UPDATE = "update ACUPOINT set DESCRIPTION_ZH = '";
  private static final String SQL_UPDATE_WHERE = "' where CODE = '";
  private static final int CODE_COLUMN_NO = 2;
  
  public static void build() throws IOException
  {
    BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(OUTPUT_FILE, true), SQLBuilder.ENCODING));
    BufferedWriter update_out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(SQLBuilder.TARGET_FILE_UPDATE_ZH_DESC_1_1_0, true), SQLBuilder.ENCODING));
    boolean isFirstRow = true;
    String initSQL = SQL_INIT0;

    try
    {
      for (int n=1; n<=CHANNEL_NUM; n++)
      {
        BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(INPUT_FILE + n  + ".txt"), SQLBuilder.DECODING));
        String row = null;
        while ((row = in.readLine()) != null)
        {
          if (row.trim().length() == 0)
            continue;
          
          // Parse one row to columns
          while (row.split("\"", -1).length % 2 == 0)
            row += "\n" + in.readLine();
          row = row.replaceAll("\"", "");
          String[] columns = row.split("\t", COLUMN_NUM);
    
          // Append INIT SQL
          out.append(initSQL);
          for (int i=0; i<BASE_COLUMN_NUM; i++)
            out.append(columns[i].trim()).append("', '");
          out.append("<<WHO>>\n").append(columns[BASE_COLUMN_NUM+0].trim());
          out.append("\n\n<<acupuncture.com>>\n").append(columns[BASE_COLUMN_NUM+1].trim());
          out.append("\n\n<<rootdown.us>>\n").append(columns[BASE_COLUMN_NUM+2].trim());
          out.append("', '");
          out.append("<<针灸学>>@TCM100.COM: \n").append(columns[BASE_COLUMN_NUM+3].trim());
          out.append("'\n");
          
          // Append UPDATE SQL -- different version, different code
          update_out.append(SQL_UPDATE);
          update_out.append("<<针灸学>>@TCM100.COM: \n");
          update_out.append(columns[BASE_COLUMN_NUM+3].trim());
          update_out.append(SQL_UPDATE_WHERE);
          update_out.append(columns[CODE_COLUMN_NO].trim());
          update_out.append("';\n");
          
          if (isFirstRow)
          {
            isFirstRow = false;
            initSQL = SQL_INIT;
          }
        }
    
        in.close();
      }
    } finally
    {
      out.append(";");
      out.close();
      update_out.close();
    }
  }
}
