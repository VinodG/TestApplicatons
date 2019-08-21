package com.taksycraft.testapplicatons.common;

import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

////////////////////////////////////////////////////////////////////////////////////////////////////////////
// DATE FORMAT:                                       | TIME FORMAT:                       | SEPARATOR:   //
// d -- 1   dd -- 01                                  | h - 9 hh - 09 H - 9&21 HH - 09&21  | .  -- dot    //
// M -- 1   MM -- 01     MMM -- jan MMMM -- january   | m - 9 mm - 09                      | -  -- hypen  //
// yy - 14  yyyy - 2014                               | s - 9 ss - 09                      | "" -- space  //
//                                                    | a - am/pm                          |  : -- colon  //
//                                                    |                                    |  / -- slash  //
// 1.Calendar calendar = Calendar.getInstance();                                                          //   
// 2.SimpleDateFormat dateFormat = new SimpleDateFormat("pattern");                                       //
// 3.String date = dateFormat.format(calendar.getTime());                                                 //
////////////////////////////////////////////////////////////////////////////////////////////////////////////


public class CalendarUtils 
{
	public static final String DATE_MAIN_AM_PM = "yyyy-MM-dd hh:mm a";
	public static final String HOUR_12 = "hh";
	public static final String HOUR_24 = "HH";
	public static final String MINUTE = "mm";
	private static final String DATE_PATTERN1 		= "yyyy-MM-dd'T'HH:mm:ss";
	private static final String DATE_PATTERN4 		= "yyyy-MM-dd HH:mm:ss";
	private static final String DATE_PATTERN5 		= "yyyy-MM-dd HH:mm";
	public static final String DATE_MAIN 		= "yyyy-MM-dd HH:mm:ss";
	public static final String  DATE_YEAR_MONTH_DATE 		= "yyyy-MM-dd";
	private static final String DATE_PATTERN2      	= "dd/MM/yyyy hh:mm:ss";
	public static final String DATE_STD_PATTERN2 = "dd-MM-yyyy HH:mm ";
	public static final String TIME_PATTERN = "HH:mm";
	public static final String DAY_DATE_MONTH_FULL = "EEEE MMMM yyyy";
	public static final String DATE_MONTH_FULL_YEAR = "dd MMMM yyyy";
	public static final String DAY_FULL= "EEEE";
	public static final String DAY_MEDIUM= "EEE";
	public static final String DATE_FULL= "dd";
	public static final String YEAR_FULL= "yyyy";
	public static final String MONTH_FULL= "MMMM";
	public static final String MONTH_MEDIUM= "MMM";
	public static final String HOUR_MIN_SEC= "hh:mm:ss";
	public static final String HOUR24_MIN_SEC= "HH:mm:ss";
	public static final String HOUR_MIN_AM_PM= "hh:mm a";
	public static final String MIN_SEC= "mm:ss";
	public static final String AM_PM= "a";

	private static final String DATE_PATTERN3      	= "MMM yyyy";
	private static final String DATE_PATTERN_MONTH      	= "MMM";

	//Get specific formatted Date(Date) from milliseconds(long) 
	public static Date getFormattedDate(long dateInMilliseconds)
	{
		Date resultDate;
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_PATTERN1,Locale.US);
	    resultDate = new Date(dateInMilliseconds);
		Date date = null;
		try {
			date = sdf.parse(sdf.format(resultDate));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	//Get specific formatted Date(Date) from milliseconds
	public static Date getDate(long milliSeconds)
	{
		 Date date = null;
		 SimpleDateFormat formatter = new SimpleDateFormat(DATE_PATTERN2,Locale.US);
	     Calendar calendar = Calendar.getInstance();
	     calendar.setTimeInMillis(milliSeconds);

	     try {
			date = formatter.parse(formatter.format(calendar.getTime()));
		} catch (ParseException e) {
			e.printStackTrace();
		}
	     return date;
	}


	
	//Get milliseconds(long) from Date(Date)
		public static long getTimeStamp(Date date)
		{
			try {
				return date.getTime()/1000;
			} catch (Exception e) {
				e.printStackTrace();
			}
			return 0;
		}
		
	//Get total duration in  hh:mm:ss format from milliseconds(long)
	public static String getDuration(long milliseconds)
	{
		int seconds = (int) (milliseconds / 1000) % 60;
		int minutes = (int) ((milliseconds / (1000 * 60)) % 60);
		int hours = (int) ((milliseconds / (1000 * 60 * 60)) % 24);
		if (hours < 1)
			return minutes + ":" + seconds;
		return hours + ":" + minutes + ":" + seconds;
	}
	
	public static long getMilliseondsFromDate(int date, int month , int year)
	{
		Calendar calendar = Calendar.getInstance();
		calendar.set(date, month, year);
		return calendar.getTimeInMillis();
	}
	
	public static String get_Time_Differece_Hour_Minutes_Seconds(long  firstTimeInMilliSeconds, long lastTimeInMilliSeconds)
	{
		return  null;
	}
	public static String getMonthAndYear(long milliseconds)
	{
		String sdf = new SimpleDateFormat(DATE_PATTERN3).format(milliseconds);
		return sdf;
	}
	public static String getMonth(long milliseconds)
	{
		String sdf = new SimpleDateFormat(DATE_PATTERN_MONTH).format(milliseconds);
		return sdf;
	}
	public static String getHourAndMinutes(long milliseconds)
	{
		String sdf = new SimpleDateFormat(TIME_PATTERN).format(milliseconds);
		return sdf;
	}
	public static String getDatePatternByMilliSecs(long milliseconds)
	{
		String sdf = new SimpleDateFormat(DATE_PATTERN5).format(milliseconds);
		return sdf;
	}
	public static String getData(long milliseconds,String format)
	{
		String sdf = new SimpleDateFormat(format,Locale.US).format(milliseconds);
		return sdf;
	}
	public static String getDataWithLocale(long milliseconds,String format)
	{
		String sdf = new SimpleDateFormat(format).format(milliseconds);
		return sdf;
	}
	public static String getPref(int d)
    {

        if (d > 3 && d < 21)
            return "th";
        switch (d % 10) {
            case 1:  return "st";
            case 2:  return "nd";
            case 3:  return "rd";
            default: return "th";
        }

    }



	public static String getCurrentDateAndtime() {
		String sdf = new SimpleDateFormat(DATE_MAIN, Locale.US).format(System.currentTimeMillis());
		return sdf;
	}




    public static long getTimeMilliSeconds(String updatedAt) {
		if((!TextUtils.isEmpty(updatedAt) )&& (!updatedAt.equalsIgnoreCase("null")))
		{
			SimpleDateFormat sdf = new SimpleDateFormat(DATE_PATTERN4,Locale.US);
			try {
				Date date = sdf.parse(updatedAt);
				return (date.getTime());
			} catch (ParseException e) {
				e.printStackTrace();
				return Calendar.getInstance().getTimeInMillis();
			}

		}
		return  Calendar.getInstance().getTimeInMillis();
    }
    public static long getTimeMilliSeconds(String updatedAt,String format) {
		if((!TextUtils.isEmpty(updatedAt) )&& (!updatedAt.equalsIgnoreCase("null")))
		{
			SimpleDateFormat sdf = new SimpleDateFormat(CalendarUtils.DATE_YEAR_MONTH_DATE+" "+format,Locale.US);
			try {
				String yyyymmdd = CalendarUtils.getData(Calendar.getInstance().getTimeInMillis(),CalendarUtils.DATE_YEAR_MONTH_DATE);
				Date date = sdf.parse(yyyymmdd+" "+updatedAt);
				return (date.getTime());
			} catch (ParseException e) {
				e.printStackTrace();
				return Calendar.getInstance().getTimeInMillis();
			}
		}
		return  Calendar.getInstance().getTimeInMillis();
    }
    public static long get24hrsTimeMilliSeconds(String updatedAt) {
		if((!TextUtils.isEmpty(updatedAt) )&& (!updatedAt.equalsIgnoreCase("null")))
		{
			SimpleDateFormat sdf = new SimpleDateFormat(DATE_PATTERN5,Locale.US);
			try {
				Date date = sdf.parse(updatedAt);
				return (date.getTime());
			} catch (ParseException e) {
				e.printStackTrace();
				return Calendar.getInstance().getTimeInMillis();
			}
		}
		return  Calendar.getInstance().getTimeInMillis();
    }
    public static String getConversion(String updatedAt,String inputFormat, String outputFormat) {
			SimpleDateFormat inFormat = new SimpleDateFormat(inputFormat,Locale.US);
			SimpleDateFormat outFormat = new SimpleDateFormat(outputFormat,Locale.US);
			try {
				Date date = inFormat.parse(updatedAt);
				Calendar calendar = Calendar.getInstance();
				calendar.setTimeInMillis(date.getTime());
				return  outFormat.format(calendar.getTime());
			} catch (ParseException e) {
				e.printStackTrace();
				return outFormat.format(Calendar.getInstance().getTime());
			}
    }
    public static String getConversionWithLocale(String updatedAt,String inputFormat, String outputFormat) {
			SimpleDateFormat inFormat = new SimpleDateFormat(inputFormat );
			SimpleDateFormat outFormat = new SimpleDateFormat(outputFormat );
			try {
				Date date = inFormat.parse(updatedAt);
				Calendar calendar = Calendar.getInstance();
				calendar.setTimeInMillis(date.getTime());
				return  outFormat.format(calendar.getTime());
			} catch (ParseException e) {
				e.printStackTrace();
				return outFormat.format(Calendar.getInstance().getTime());
			}
    }

	public static String get12HrFormatFromMilliseconds(long milliseconds)
	{
		String sdf = new SimpleDateFormat(DAY_FULL).format(milliseconds);
		return sdf;
	}


}



