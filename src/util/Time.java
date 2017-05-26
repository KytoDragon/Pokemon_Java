package util;

public class Time {

	public static long getTime() {
		return ifloordiv(System.currentTimeMillis(), 1000L);
	}

	/** Uses the Gregorian calender, even for dates before its invention */
	public static int[] timestamp_to_gmt(long ts) {
		int[] res = new int[6];
		long s = ifloormod(ts, 86400);
		ts = ifloordiv(ts, 86400);
		res[0] = (int) (s % 60);
		res[1] = (int) (s / 60 % 60);
		res[2] = (int) (s / 3600);

		long x = ifloordiv(ts * 4 + 102032, 146097) + 15;
		long days = ts + 2442113 + x - ifloordiv(x, 4);
		long year = ifloordiv((days * 20 - 2442), 7305);
		long day = days - 365 * year - ifloordiv(year, 4);
		long month = day * 1000 / 30601;
		res[3] = (int) (day - month * 30 - month * 601 / 1000);
		if (month < 14) {
			res[4] = (int) (month - 1);
			res[5] = (int) (year - 4716);
		} else {
			res[4] = (int) (month - 13);
			res[5] = (int) (year - 4715);
		}
		return res;
	}

	/** Works starting 01.03.1900 00:00:00 */
	private static void timestamp_to_gmt_civil_orig(long ts, long[] res) {
		long s = ts % 86400;
		ts = ts / 86400;
		res[0] = s % 60;
		res[1] = s / 60 % 60;
		res[2] = s / 3600;

		long x = (ts * 4 + 102032) / 146097 + 15;
		long days = ts + 2442113 + x - x / 4;
		long year = (days * 20 - 2442) / 7305;
		long day = days - 365 * year - year / 4;
		long month = day * 1000 / 30601;
		res[3] = day - month * 30 - month * 601 / 1000;
		if (month < 14) {
			res[4] = month - 1;
			res[5] = year - 4716;
		} else {
			res[4] = month - 13;
			res[5] = year - 4715;
		}
	}

	private static long ifloordiv(long n, long d) {
		if (n >= 0) {
			return n / d;
		} else {
			return ~(~n / d);
		}
	}

	private static long ifloormod(long n, long d) {
		if (n >= 0) {
			return n % d;
		} else {
			return d + ~(~n % d);
		}
	}

	public static long gmt_to_timestamp(int[] res) {
		long y = res[5];
		long m = res[4];
		if (m <= 2) {
			y -= 1;
			m += 12;
		}
		// 3 * (m + 1) / 5 - 3	calculates the sum of days the previous months went over 30.
		return (365 * y + ifloordiv(y, 4) - ifloordiv(y, 100) + ifloordiv(y, 400) + (3 * (m + 1) / 5 - 3) + 30 * m + res[3] - 719558L) * 86400L + 3600L * res[2] + 60L * res[1] + res[0];
	}
}
