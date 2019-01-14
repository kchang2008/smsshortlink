package sms.imobpay.com.testsmslinkcore;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.SmsManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;

import static sms.imobpay.com.testsmslinkcore.R.id.field_value_et;

/**
 *  主程序：配置跳转附加地址，短链生成器调用
 */
public class SmsLinkCoreActivity extends Activity implements View.OnClickListener,TextWatcher{
    private String TAG = "SmsLinkCoreActivity";
    private String http_url_head = "http://www.imobpay.com/";
    private String http_url_params = "?sms_extras=";
    private String rqb_url_page = "/ruiqianbaoSms.html";
    private String rtb_url_page = "/ruitongbaoSms.html";
    private String url_host ="",url_suffix = "";
    private TextView url_tv;
    private EditText func_type_et,phone_num_et,url_host_et;
    private Spinner app_selcet_sp;
    private LinearLayout h5_layout,diasplay_layout,local_layout;
    private Button send_sms_bt,make_short_url_bt;
    private String sms_extra = "";
    private String short_url = "";

    private int[][] sms_fields= new int[][]{
        {R.id.classNameByAndroid_layout,R.string.classNameByAndroid},
        {R.id.classNameByIOS_layout,R.string.classNameByIOS},

        {R.id.backButtonName_layout,R.string.backButtonName},
        {R.id.titleNameColor_layout,R.string.titleNameColor},
        {R.id.backButtonNameColor_layout,R.string.backButtonNameColor},
        {R.id.startPage_layout,R.string.startPage},

        {R.id.splitLineColor_layout,R.string.splitLineColor},
        {R.id.tipsUrl_layout,R.string.tipsUrl},
        {R.id.backType_layout,R.string.backType},
        {R.id.tipsButtonName_layout,R.string.tipsButtonName},

        {R.id.bottomTabFlag_layout,R.string.bottomTabFlag},
        {R.id.pathFolder_layout,R.string.pathFolder},
        {R.id.titleName_layout,R.string.titleName},
        {R.id.tipsButtonNameColor_layout,R.string.tipsButtonNameColor},

        {R.id.titlebarColor_layout,R.string.titlebarColor},
        {R.id.thirdPartyUrl_layout,R.string.thirdPartyUrl},
        {R.id.messageDetail_layout,R.string.messageDetail},
        {R.id.messageTitle_layout,R.string.messageTitle}

    };

    private final int SHOW_URL_WHAT = 0 ; //显示短链
    private final int SEND_SMS_WHAT = 1 ; //发送短信

    /**
     * 判断是否需要检测，防止不停的弹框
     */
    private boolean isNeedCheck = true;

    private Handler smsHandle = new Handler(){
        @Override
        public void handleMessage(Message message){
            switch (message.what){
                case SHOW_URL_WHAT:
                    onShowUrl();
                    break;
                case SEND_SMS_WHAT:
                    doSendSMSTo();
                    break;
            }
        }
    };

    private final int PERMISSON_REQUESTCODE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms_link_core);
        initView();
    }

    private void initSmsField(){
        url_tv = (TextView) findViewById(R.id.url_tv);
        func_type_et = (EditText) findViewById(R.id.func_type_et);
        phone_num_et = (EditText) findViewById(R.id.phone_num_et);
        url_host_et = (EditText) findViewById(R.id.url_host_et);

        for (int i = 0 ;i <sms_fields.length;i++){
            int id = sms_fields[i][0];
            LinearLayout field_layout = (LinearLayout)findViewById(id);
            TextView field_name_tv = (TextView)field_layout.findViewById(R.id.field_name_tv);
            EditText field_value_et = (EditText) field_layout.findViewById(R.id.field_value_et);
            if ( id == R.id.backButtonName_layout){
                field_value_et.setText(getString(R.string.back));
            } else if ( id == R.id.titleNameColor_layout){
                field_value_et.setText(getString(R.string.title_color));
            } else if ( id == R.id.backButtonNameColor_layout){
                field_value_et.setText(getString(R.string.back_bt_color));
            } else if ( id == R.id.splitLineColor_layout){
                field_value_et.setText(getString(R.string.split_color));
            } else if ( id == R.id.titlebarColor_layout){
                field_value_et.setText(getString(R.string.title_bar_color));
            } else if ( id == R.id.backType_layout){
                field_value_et.setText("2");
            } else if ( id == R.id.startPage_layout){
                field_value_et.setText("index.html");
            } else if ( id == R.id.bottomTabFlag_layout){
                field_value_et.setText("1");
            }
            field_name_tv.setText(getString(sms_fields[i][1]));
        }

        local_layout = (LinearLayout) findViewById(R.id.local_layout);
        local_layout.setVisibility(View.GONE);

        h5_layout = (LinearLayout) findViewById(R.id.h5_layout);
        h5_layout.setVisibility(View.GONE);

        diasplay_layout = (LinearLayout) findViewById(R.id.diasplay_layout);
        diasplay_layout.setVisibility(View.GONE);
    }

    private void initView(){
        initSmsField();

        app_selcet_sp = (Spinner) findViewById(R.id.app_selcet_sp);
        app_selcet_sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                url_host_et.setText("");
                if ( position == 0) {  //瑞通宝
                    url_suffix = rtb_url_page;
                    url_host_et.setText(SmsLinkCoreActivity.this.getString(R.string.rtb_http_url));
                } else {  //瑞钱包'
                    url_suffix = rqb_url_page;
                    url_host_et.setText(SmsLinkCoreActivity.this.getString(R.string.rqb_http_url)+rqb_url_page);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        send_sms_bt = (Button)findViewById(R.id.send_sms_bt);
        send_sms_bt.setOnClickListener(this);

        make_short_url_bt = (Button) findViewById(R.id.make_short_url_bt);
        make_short_url_bt.setOnClickListener(this);

        //设置监听事件
        func_type_et.addTextChangedListener(this);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if ( null != s ) {
            String ss = s.toString();
            if ("0".equals(ss)){
                h5_layout.setVisibility(View.GONE);
                diasplay_layout.setVisibility(View.GONE);
                local_layout.setVisibility(View.VISIBLE);
            } else if ( "1".equals(ss) || "2".equals(ss)) {
                h5_layout.setVisibility(View.VISIBLE);
                diasplay_layout.setVisibility(View.GONE);
                local_layout.setVisibility(View.GONE);
            } else if ("3".equals(ss)) {
                h5_layout.setVisibility(View.GONE);
                local_layout.setVisibility(View.GONE);
                diasplay_layout.setVisibility(View.VISIBLE);
            } else {
                h5_layout.setVisibility(View.GONE);
                local_layout.setVisibility(View.GONE);
                diasplay_layout.setVisibility(View.GONE);
            }
        } else {
            h5_layout.setVisibility(View.GONE);
            diasplay_layout.setVisibility(View.GONE);
            local_layout.setVisibility(View.GONE);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isNeedCheck) {
            PermissionService.getInstance(this).checkPermissions();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] paramArrayOfInt) {
        if (requestCode == PERMISSON_REQUESTCODE) {
            if (!PermissionService.getInstance(this).verifyPermissions(paramArrayOfInt)) {
                isNeedCheck = false;
            }
        }
    }

    /**
     * 复制到系统剪切板
     */
    private void onShowUrl() {
        // 从API11开始android推荐使用android.content.ClipboardManager
        // 为了兼容低版本我们这里使用旧版的android.text.ClipboardManager，虽然提示deprecated，但不影响使用。
        //ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        // 将文本内容放到系统剪贴板里。
        //cm.setText(short_url);

        url_tv.setText(short_url);

        // uc浏览器："com.uc.browser", "com.uc.browser.ActivityUpdate"
        // opera："com.opera.mini.android", "com.opera.mini.android.Browser"
        // qq浏览器："com.tencent.mtt", "com.tencent.mtt.MainActivity"

        //从其他浏览器打开
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        Uri content_url = Uri.parse(short_url);
        intent.setData(content_url);
        startActivity(Intent.createChooser(intent, "请选择浏览器"));
    }

    /**
     * 把一组数据添加到JSON对象中
     * @param extras
     * @param key_res_id:key名称定义在resource中的extras_field.xml文件里
     * @param value
     */
    private void addParmas(JSONObject extras,int key_res_id,String value){
        try {
            if (StringUtils.isNotEmptyOrNull(value)) {
                extras.put(getString(key_res_id), value);
            }
        }catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * 添加h5的参数信息
     * @param extras
     */
    private void addSmsFieldParams(JSONObject extras){
        try {
            for (int i = 0 ;i <sms_fields.length;i++) {
                LinearLayout field_layout = (LinearLayout) findViewById(sms_fields[i][0]);
                EditText field_value = (EditText) field_layout.findViewById(field_value_et);
                if (null != field_value.getText()) {
                    String val = field_value.getText().toString();
                    int id = field_layout.getId();
                    if (( id == R.id.titleNameColor_layout)|| ( id == R.id.backButtonNameColor_layout)
                            || ( id == R.id.splitLineColor_layout)|| ( id == R.id.titlebarColor_layout)){
                        if (!StringUtils.checkColor(val)) {
                            val = "#"+val;
                        }
                    }
                    if (StringUtils.isNotEmptyOrNull(val)) {
                        extras.put(getString(sms_fields[i][1]), val);
                    }
                }
            }
        }catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view){
        int id =  view.getId();
        if (id == make_short_url_bt.getId()) {
            makeShortUrl(SHOW_URL_WHAT);
        } else if (id == send_sms_bt.getId()) {
            makeShortUrl(SEND_SMS_WHAT);
        }
    }

    /**
     * 生成短链
     * @param what
     */
    private void makeShortUrl(final int what){
        String funcType = func_type_et.getText().toString();

        JSONObject extras = new JSONObject();
        addParmas(extras,R.string.funcType,funcType);

        addSmsFieldParams(extras);

        if ( !StringUtils.isNotEmptyOrNull(funcType) || extras.length() == 0 ) {
            Log.i(TAG,"无数据");
            url_tv.setText("数据未配置");
            return;
        }

        url_host = url_host_et.getText().toString();

        //不为空，且不能没有?
        if ( !StringUtils.isNotEmptyOrNull(url_host)) {
            Log.i(TAG,"请求地址不合法");
            Toast.makeText(this, "请求地址不合法",Toast.LENGTH_LONG).show();
            return;
        }

        sms_extra = Base64Utils.encode(URLEncoder.encode(extras.toString()).getBytes());
        sms_extra = sms_extra.replace("=","%3D");
        Log.i(TAG,"sms_extra = "+sms_extra);

        new Thread(){
            @Override
            public void run(){
                String url = http_url_head + url_host + url_suffix + http_url_params + sms_extra;

                short_url = ShortUrlHelper.getShortUrl(url);
                Log.i(TAG,"操作成功，短链是"+short_url);
                smsHandle.sendEmptyMessage(what);
            }
        }.start();
    }
    /**
     * 调起系统发短信功能 :考虑到长短信可能，采用彩信方式发送
     *
     */
    public void doSendSMSTo() {
        String message = short_url;
        if ( null != phone_num_et.getText()) {
            String phoneNumber = phone_num_et.getText().toString();
            if (StringUtils.isNotEmptyOrNull(phoneNumber) && phoneNumber.length() == 11) {
                SmsManager manager = SmsManager.getDefault();
                manager.sendTextMessage(phoneNumber,null,message,null,null);
                send_sms_bt.setClickable(true);
            }
        }

    }
}
