package com.airsme.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.airsme.R;
import com.airsme.appconfig.GlobalStorage;
import com.airsme.models.Proxy;
import com.airsme.models.Tender;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;

import java.math.BigDecimal;


public class BProxyInfor extends AppCompatActivity {

    public static Proxy proxy;
    public static Tender subject;

    private static final String TAG = "paymentExample";
    /**
     * - Set to PayPalConfiguration.ENVIRONMENT_PRODUCTION to move real money.
     *
     * - Set to PayPalConfiguration.ENVIRONMENT_SANDBOX to use your test credentials
     * from https://developer.paypal.com
     *
     * - Set to PayPalConfiguration.ENVIRONMENT_NO_NETWORK to kick the tires
     * without communicating to PayPal's servers.
     */
    private static final String CONFIG_ENVIRONMENT = PayPalConfiguration.ENVIRONMENT_NO_NETWORK;

    // note that these credentials will differ between live & sandbox environments.
    private static final String CONFIG_CLIENT_ID = "credentials from developer.paypal.com";

    private static final int REQUEST_CODE_PAYMENT = 1;
    private static final int REQUEST_CODE_FUTURE_PAYMENT = 2;
    private static final int REQUEST_CODE_PROFILE_SHARING = 3;

    private static PayPalConfiguration config = new PayPalConfiguration()
            .environment(CONFIG_ENVIRONMENT)
            .clientId(CONFIG_CLIENT_ID)
            // The following are only used in PayPalFuturePaymentActivity.
            .merchantName("Example Merchant")
            .merchantPrivacyPolicyUri(Uri.parse("https://www.example.com/privacy"))
            .merchantUserAgreementUri(Uri.parse("https://www.example.com/legal"));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bproxy_infor);

        TextView name=findViewById(R.id.pname);
        name.setEnabled(false);
        name.setText(proxy.getTitle()+" "+proxy.getName()+" "+proxy.getSurname());
        TextView edu=findViewById(R.id.peducation);
        edu.setEnabled(false);
        edu.setText(proxy.getEducation());
        TextView lang=findViewById(R.id.planguages);
        lang.setEnabled(false);
        lang.setText(proxy.getLanguages());
        TextView prof=findViewById(R.id.pprofesion);
        prof.setEnabled(false);
        prof.setText(proxy.getProfession());
        TextView about=findViewById(R.id.bproxy_infor_text);
        about.setEnabled(false);
        about.setText(proxy.getAboutme());
        Button b=findViewById(R.id.bproxy_infor_paymentbtn);

        ImageView profile = findViewById(R.id.pimage);
        profile.setPadding(0,0,0,0);
        //prof.setCropToPadding(true);
        profile.setImageResource(R.drawable.com_facebook_profile_picture_blank_portrait);
        new GlobalStorage(this).loadImage(proxy.getPic(), profile);

        b.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               pay();
           }
       });

        // new RoundViews(this).themeControls((LinearLayout) findViewById(R.id.bproxyinfor_main));
        getSupportActionBar().setTitle(proxy.getName());
    }


    private void pay(){
        onBuyPressed();
    }
    public void onBuyPressed() {
        /*
         * PAYMENT_INTENT_SALE will cause the payment to complete immediately.
         * Change PAYMENT_INTENT_SALE to
         *   - PAYMENT_INTENT_AUTHORIZE to only authorize payment and capture funds later.
         *   - PAYMENT_INTENT_ORDER to create a payment for authorization and capture
         *     later via calls from your server.
         *
         * Also, to include additional payment details and an item list, see getStuffToBuy() below.
         */
        PayPalPayment thingToBuy = getThingToBuy(PayPalPayment.PAYMENT_INTENT_SALE);


        /*
         * See getStuffToBuy(..) for examples of some available payment options.
         */

        Intent intent = new Intent(BProxyInfor.this, PaymentActivity.class);

        // send the same configuration for restart resiliency
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);

        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, thingToBuy);

        startActivityForResult(intent, REQUEST_CODE_PAYMENT);
    }

    private PayPalPayment getThingToBuy(String paymentIntent) {
        final double cost = MapsMarkerActivity.calculateCost(proxy.jgetMaplocation(), subject.jgetMaplocation());
        return new PayPalPayment(new BigDecimal(cost), "USD", "Amount",
                paymentIntent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


    }
}
 /*private void pay2(){
        MposUi ui = MposUi.initialize(this, ProviderMode.MOCK,
                "merchantIdentifier", "merchantSecretKey");

        ui.getConfiguration().setSummaryFeatures(EnumSet.of(
                // Add this line, if you do want to offer printed receipts
                // MposUiConfiguration.SummaryFeature.PRINT_RECEIPT,
                MposUiConfiguration.SummaryFeature.SEND_RECEIPT_VIA_EMAIL)
        );

        // Start with a mocked card reader:
        AccessoryParameters accessoryParameters = new AccessoryParameters.Builder(AccessoryFamily.MOCK)
                .mocked()
                .build();
        ui.getConfiguration().setTerminalParameters(accessoryParameters);

        // Add this line if you would like to collect the customer signature on the receipt (as opposed to the digital signature)
        // ui.getConfiguration().setSignatureCapture(MposUiConfiguration.SignatureCapture.ON_RECEIPT);


    /* When using the Bluetooth Miura, use the following parameters:
    AccessoryParameters accessoryParameters = new AccessoryParameters.Builder(AccessoryFamily.MIURA_MPI)
                                                                     .bluetooth()
                                                                     .build();
    ui.getConfiguration().setTerminalParameters(accessoryParameters);
    */




    /* When using Verifone readers via WiFi or Ethernet, use the following parameters:
    AccessoryParameters accessoryParameters = new AccessoryParameters.Builder(AccessoryFamily.VERIFONE_VIPA)
                                                                     .tcp("192.168.254.123", 16107)
                                                                     .build();
    ui.getConfiguration().setTerminalParameters(accessoryParameters);
    *********** //

        TransactionParameters transactionParameters = new TransactionParameters.Builder()
                .charge(new BigDecimal(""+MapsMarkerActivity.calculateCost(proxy.jgetMaplocation(), subject.jgetMaplocation())), Currency.ZAR)
                .subject(proxy.getName())
                .customIdentifier("yourReferenceForTheTransaction")
                .build();

        Intent intent = ui.createTransactionIntent(transactionParameters);
        startActivityForResult(intent, MposUi.REQUEST_CODE_PAYMENT);
    }*/