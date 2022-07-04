package com.busybees.lauk_kaing_expert_services.utility;

import android.content.Context;
import android.net.ConnectivityManager;
import android.text.Editable;
import android.text.Html;
import android.text.style.BulletSpan;
import android.text.style.LeadingMarginSpan;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.myanmartools.TransliterateZ2U;
import com.google.myanmartools.ZawgyiDetector;

import org.xml.sax.XMLReader;

import java.util.ArrayList;
import java.util.List;

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

    public static void changeFontZg2UniHome(TextView v, String changeFontString){

        TransliterateZ2U z2U = new TransliterateZ2U("Zawgyi to Unicode");
        String output = z2U.convert(changeFontString);
        v.setText(output);

    }

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
}
