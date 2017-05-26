package sequenze;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Timer {

	public static String getTimeString() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss_SSS");
		return sdf.format(new Date());
	}

}
