package com.busybees.little_bee.utility;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.text.Editable;
import android.text.Html;
import android.text.style.BulletSpan;
import android.text.style.LeadingMarginSpan;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.busybees.little_bee.data.vos.Users.UserVO;
import com.google.gson.Gson;

import org.xml.sax.XMLReader;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import me.myatminsoe.mdetect.Rabbit;

public class Utility {

    public static boolean isOnline(Context ctx) {
        ConnectivityManager cm = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        boolean hostReachable = false;

        if (cm != null && cm.getActiveNetworkInfo() != null)
            hostReachable = cm.getActiveNetworkInfo().isConnectedOrConnecting();
        return hostReachable;
    }

    public static void showToast(Context context, String message) {
        Toast toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
        ViewGroup group = (ViewGroup) toast.getView();
        if (group != null) {
            TextView tvMessage = (TextView) group.getChildAt(0);
            tvMessage.setText(message);
            tvMessage.setGravity(Gravity.CENTER);
            toast.show();
        } else {
            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
        }
    }

    public static int dp2px(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5F);
    }

    public static void changeFontUni2Zg(TextView v, String chagneFontString){
        v.setText(Rabbit.uni2zg(chagneFontString));

    }

    public static void addFontSu(TextView v, String chagneFontString){
        v.setText(chagneFontString);

    }

    public static void changeFontUni2ZgHome(TextView v, String changeFontString){

        v.setText(Rabbit.uni2zg(changeFontString));

    }

    public static String changeFontUni2ZgString(String chagneFontString) {
        return Rabbit.uni2zg(chagneFontString);

    }

    /*public static void changeFontZg2UniHome(TextView v, String changeFontString){

        TransliterateZ2U z2U = new TransliterateZ2U("Zawgyi to Unicode");
        String output = z2U.convert(changeFontString);
        v.setText(output);

    }*/

    public static void addFontSuHome(TextView v, String changeFontString){
        String s = changeFontString;
        String[] parts = s.split(" ");
        if(parts.length==2){
            String p1 = parts[0];
            String p2=parts[1];
            String hi = p1 +"\n" + p2;
            v.setText(hi);
        }else{
            v.setText(changeFontString);
        }
    }

    public static class UlTagHandler implements Html.TagHandler {
        private int                 m_index     = 0;
        private List< String > m_parents   = new ArrayList< String >( );

        @Override
        public void handleTag(final boolean opening, final String tag, Editable output, final XMLReader xmlReader )
        {
            if( tag.equals( "ul" ) || tag.equals( "ol" ) || tag.equals( "dd" ) )
            {
                if( opening )
                {
                    m_parents.add( tag );
                }
                else m_parents.remove( tag );

                m_index = 0;
            }
            else if( tag.equals( "li" ) && !opening ) handleListTag( output );
        }

        private void handleListTag( Editable output )
        {
            if( m_parents.get(m_parents.size()-1 ).equals( "ul" ) )
            {
                output.append( "\n" );
                String[ ] split = output.toString( ).split( "\n" );

                int lastIndex = split.length - 1;
                int start = output.length( ) - split[ lastIndex ].length( ) - 1;
                output.setSpan( new BulletSpan( 15 * m_parents.size( ) ), start, output.length( ), 0 );
            }
            else if( m_parents.get(m_parents.size()-1).equals( "ol" ) )
            {
                m_index++ ;

                output.append( "\n" );
                String[ ] split = output.toString( ).split( "\n" );

                int lastIndex = split.length - 1;
                int start = output.length( ) - split[ lastIndex ].length( ) - 1;
                output.insert( start, m_index + ". " );
                output.setSpan( new LeadingMarginSpan.Standard( 15 * m_parents.size( ) ), start, output.length( ), 0 );
            }
        }
    }

    public static void Save_UserProfile(Context context, UserVO userObj){
        SharedPreferences pref=context.getSharedPreferences(Constant.SharePref, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String jsonobject = gson.toJson(userObj);
        pref.edit().putString("userobj",jsonobject).apply();
    }

    public static void delete_UserProfile(Context context){
        SharedPreferences pref=context.getSharedPreferences(Constant.SharePref, Context.MODE_PRIVATE);
        pref.edit().remove("userobj").commit();

    }

    public static UserVO query_UserProfile (Context context) {
        UserVO userVO = new UserVO();
        SharedPreferences pref=context.getSharedPreferences(Constant.SharePref, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = pref.getString("userobj", "");
        userVO = gson.fromJson(json, UserVO.class);
        return userVO;
    }

    private static String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
    private static SimpleDateFormat newdate = new SimpleDateFormat("EEE, d MMM yyyy",  Locale.ENGLISH);
    private static SimpleDateFormat newdate1 = new SimpleDateFormat("yyyy-MM-dd",  Locale.ENGLISH);

    public static void setDate1(TextView dd , String dateString) {
        try {

            Date date=null;
            try {
                //DateFormat df6 = new SimpleDateFormat("E, MMM dd yyyy");
                SimpleDateFormat df6 = new SimpleDateFormat("E, MMM dd yyyy", Locale.ENGLISH);
                Date d6 = df6.parse(dateString);
                System.out.println("Date: " + d6);
                System.out.println("Date in E, E, MMM dd yyyy HH:mm:ss format is: "+df6.format(d6));
                dd.setTag(newdate1.format(d6.getTime()));
                dd.setText(newdate.format(d6.getTime()));
                //     dd.setText(df6.format(d6.getTime()));

            } catch (Exception e){ e.printStackTrace(); }


            System.out.println("check..." + date);



        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String setDate2(TextView dd , String dateString) {
        String rdata="";
        try {

            Date date=null;
            try {
                SimpleDateFormat df6 = new SimpleDateFormat("E, MMM dd yyyy",Locale.ENGLISH);
                Date d6 = df6.parse(dateString);
                d6 = DateUtil.addDays(d6, 1);



                System.out.println("Date: " + d6);
                System.out.println("Date in E, E, MMM dd yyyy HH:mm:ss format is: "+df6.format(d6));

                dd.setTag(newdate1.format(d6.getTime()));
                dd.setText(newdate.format(d6.getTime()));
                rdata=df6.format(d6);
                return rdata;
            } catch (Exception e){ e.printStackTrace(); }



        } catch (Exception e) {
            e.printStackTrace();
        }
        return rdata;
    }

    public static void setDate(TextView date) {
        date.setTag(newdate1.format(getNextDate().getTime()));
        date.setText(newdate.format(getNextDate().getTime()));
    }

    private static Date getNextDate() {
        Calendar calendar = Calendar.getInstance();
        //calendar.add(Calendar.DAY_OF_YEAR, 1);
        calendar.add(Calendar.DAY_OF_YEAR, 0);
        //    Log.d("date....",String.valueOf(calendar.getTime()));
        return calendar.getTime();
    }

    public static void getDate (Context con, final TextView text,String dateString) {
        // List<String> locale = Locale.ENGLISH;
        String[] dateYear = text.getText().toString().split(",")[1].trim().trim().split(" ");

        int toyear = Integer.parseInt(dateYear[2].trim());
        int tomonth = getMonthByCode(dateYear[1].trim());
        int today = Integer.parseInt(dateYear[0].trim());
        Calendar newDate = Calendar.getInstance();

        Locale locale = Locale.ENGLISH;
        Locale.setDefault(locale);
        Configuration configuration = con.getResources().getConfiguration();
        configuration.setLocale(locale);
        configuration.setLayoutDirection(locale);
        con.createConfigurationContext(configuration);

        final DatePickerDialog fromDatePickerDialog = new DatePickerDialog(con, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                String dateOfBirth1 = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;

                newDate.set(year, monthOfYear, dayOfMonth);

                text.setTag(newdate1.format(newDate.getTime()));
                text.setText(newdate.format(newDate.getTime()));

            }
        },toyear, tomonth, today);

        fromDatePickerDialog.setButton(DatePickerDialog.BUTTON_POSITIVE, "OK", fromDatePickerDialog);
        fromDatePickerDialog.setButton(DatePickerDialog.BUTTON_NEGATIVE, "Cancel", fromDatePickerDialog);
        SimpleDateFormat df6 = new SimpleDateFormat("E, MMM dd yyyy",Locale.ENGLISH);

        Date d6 = null;
        try {
            d6 = df6.parse(dateString);
            fromDatePickerDialog.getDatePicker().setMinDate(d6.getTime());
            fromDatePickerDialog.show();

        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    public static int getMonthByCode(String month) {
        if (month.equalsIgnoreCase(months[0])) {
            return 0;
        } else if (month.equalsIgnoreCase(months[1])) {
            return 1;
        } else if (month.equalsIgnoreCase(months[2])) {
            return 2;
        } else if (month.equalsIgnoreCase(months[3])) {
            return 3;
        } else if (month.equalsIgnoreCase(months[4])) {
            return 4;
        } else if (month.equalsIgnoreCase(months[5])) {
            return 5;
        } else if (month.equalsIgnoreCase(months[6])) {
            return 6;
        } else if (month.equalsIgnoreCase(months[7])) {
            return 7;
        } else if (month.equalsIgnoreCase(months[8])) {
            return 8;
        } else if (month.equalsIgnoreCase(months[9])) {
            return 9;
        } else if (month.equalsIgnoreCase(months[10])) {
            return 10;
        } else if (month.equalsIgnoreCase(months[11])) {
            return 11;
        } else {
            return -1;
        }
    }

    public static String checkLng(Context activity){
        String lang = AppStorePreferences.getString(activity, AppENUM.LANG);
        if(lang == null){
            lang="en";
        }
        return lang;
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
