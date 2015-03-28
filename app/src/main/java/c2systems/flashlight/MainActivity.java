package c2systems.flashlight;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


public class MainActivity extends ActionBarActivity {

    public static Camera cam = null;
    public static Camera.Parameters params;
    public static boolean lightOn = false;
    protected static Button LED = null;

    @Override
    protected  void onStop(){
        super.onStop();
        if(cam != null){
            cam.release();
            cam = null;
            lightOn = false;
            if(LED != null){
                LED.setText(R.string.LED_OFF);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    //@Override
    //public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
    //    getMenuInflater().inflate(R.menu.menu_main, menu);
    //    return true;
    //}

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void changeLight(View view){
        if(LED == null) {
            LED = (Button) view.findViewById(view.getId());
        }

        if(this.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)) {
            if(cam == null) {
                try {
                    cam = Camera.open();
                    params = cam.getParameters();
                }
                catch(Exception ex) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Error");
                    builder.setMessage("Failed to Access Camera");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).create().show();
                }
                    return;
                }
            if(!lightOn){
                params = cam.getParameters();
                params.setFlashMode(Camera.Parameters.FOCUS_MODE_MACRO);
                params.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);

                cam.setParameters(params);
                cam.startPreview();
                lightOn = true;
                LED.setText(R.string.LED_ON);

            } else{
                params = cam.getParameters();
                params.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                cam.setParameters(params);
                cam.stopPreview();
                lightOn = false;
                LED.setText(R.string.LED_OFF);
            }
        }
    }
}
