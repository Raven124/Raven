package ibanzay.therapist;


import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    ImageButton imgbtn1;
    ImageButton imgbtn2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imgbtn1 = (ImageButton) findViewById(R.id.imgbtn1);
        imgbtn1.setOnClickListener(this);

        imgbtn2 = (ImageButton) findViewById(R.id.imgbtn2);
        imgbtn2.setOnClickListener(this);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.about_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        switch (id) {
            case R.id.about_therapist:
                createAboutBox();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void createAboutBox() {
        Dialog aboutBox = new Dialog(this);
        aboutBox.setCanceledOnTouchOutside(true);
        aboutBox.requestWindowFeature(Window.FEATURE_NO_TITLE);
        aboutBox.getWindow().setBackgroundDrawableResource(R.drawable.about_box);
        aboutBox.setContentView(R.layout.about);
        aboutBox.show();
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgbtn1: {
                Intent intent = new Intent(this, Main.class);
                startActivity(intent);
                break;
            }
            case R.id.imgbtn2:
                Intent intent = new Intent(this, Directory.class);
                startActivity(intent);
                break;
            default:
                break;

        }
    }
}
