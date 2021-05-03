package com.code.travellog.core.view.mine;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.code.travellog.R;
import com.danielstone.materialaboutlibrary.ConvenienceBuilder;
import com.danielstone.materialaboutlibrary.MaterialAboutActivity;
import com.danielstone.materialaboutlibrary.items.MaterialAboutActionItem;
import com.danielstone.materialaboutlibrary.items.MaterialAboutItemOnClickAction;
import com.danielstone.materialaboutlibrary.items.MaterialAboutTitleItem;
import com.danielstone.materialaboutlibrary.model.MaterialAboutCard;
import com.danielstone.materialaboutlibrary.model.MaterialAboutList;
import com.danielstone.materialaboutlibrary.util.ViewTypeManager;
import com.gyf.immersionbar.ImmersionBar;
import com.leon.lib.settingview.LSettingItem;
import com.mikepenz.community_material_typeface_library.CommunityMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.mvvm.base.BaseActivity;

/**
 * @description : 关于 界面
 * @date: 2021/2/23
 */
public class AboutActivity extends MaterialAboutActivity {
    protected RelativeLayout mTitleBar;
    protected TextView mTitle;
    public static final String THEME_EXTRA = "";
    public static final int THEME_LIGHT = 0;
    public static final int THEME_DARK = 1;
    public static final int THEME_DAYNIGHT = 2;
    public static final int THEME_CUSTOM_CARDVIEW = 3;

    @NonNull
    @Override
    protected MaterialAboutList getMaterialAboutList(@NonNull final Context c) {
        return createMaterialAboutList(c, getIntent().getIntExtra(THEME_EXTRA, THEME_LIGHT));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        setTheme(R.style.AppTheme_MaterialAboutActivity_Light);
        ImmersionBar.with(this).statusBarDarkFont(true).init();
//        switch (getIntent().getIntExtra(THEME_EXTRA, THEME_LIGHT)) {
//            case THEME_LIGHT:
//                setTheme(R.style.AppTheme_MaterialAboutActivity_Light);
//                break;
//            case THEME_DARK:
//                setTheme(R.style.MyCheckBox);
//                break;
//            case THEME_DAYNIGHT:
//                setTheme(R.style.MyCheckBox);
//                break;
//            case THEME_CUSTOM_CARDVIEW:
//                setTheme(R.style.MyCheckBox);
//                break;
//        }

        super.onCreate(savedInstanceState);

//        Call this method to let the scrollbar scroll off screen
//        setScrollToolbar(true);
    }

    @Override
    protected CharSequence getActivityTitle() {
        return "关于";
//        return getString(R.string.mal_title_about);
    }

    @NonNull
    @Override
    protected ViewTypeManager getViewTypeManager() {
        return super.getViewTypeManager();
    }

        public static MaterialAboutList createMaterialAboutList(final Context c, final int theme) {
            MaterialAboutCard.Builder appCardBuilder = new MaterialAboutCard.Builder();

            // Add items to card

            appCardBuilder.addItem(new MaterialAboutTitleItem.Builder()
                    .text("Travel Log")
                    .desc("© 2021 弄影到三山")
                    .icon(R.mipmap.ic_launcher)
                    .build());

            appCardBuilder.addItem(ConvenienceBuilder.createVersionActionItem(c,
                    new IconicsDrawable(c)
                            .icon(CommunityMaterial.Icon.cmd_information_outline)
                            .sizeDp(18),
                    "Version",
                    true));


            appCardBuilder.addItem(new MaterialAboutActionItem.Builder()
                    .text("Licenses")
                    .icon(new IconicsDrawable(c)
                            .icon(CommunityMaterial.Icon.cmd_book)
                            .sizeDp(18))
                    .setOnClickAction(new MaterialAboutItemOnClickAction() {
                        @Override
                        public void onClick() {
//                        Intent intent = new Intent(c, ExampleMaterialAboutLicenseActivity.class);
//                        intent.putExtra(ExampleMaterialAboutActivity.THEME_EXTRA, theme);
//                        c.startActivity(intent);
                        }
                    })
                    .build());

            MaterialAboutCard.Builder authorCardBuilder = new MaterialAboutCard.Builder();
            authorCardBuilder.title("Author");
            authorCardBuilder.titleColor(ContextCompat.getColor(c, R.color.colorAccent));

            authorCardBuilder.addItem(new MaterialAboutActionItem.Builder()
                    .text("弄影到三山团队")
                    .icon(new IconicsDrawable(c)
                            .icon(CommunityMaterial.Icon.cmd_account)
                            .sizeDp(18))
                    .build());

            MaterialAboutCard.Builder convenienceCardBuilder = new MaterialAboutCard.Builder();

            MaterialAboutCard.Builder otherCardBuilder = new MaterialAboutCard.Builder();
            otherCardBuilder.title("更多");
//            otherCardBuilder.outline(false);

            otherCardBuilder.cardColor(Color.parseColor("#c0cfff"));

            otherCardBuilder.addItem(new MaterialAboutActionItem.Builder()
                    .icon(new IconicsDrawable(c)
                            .icon(CommunityMaterial.Icon.cmd_language_html5)
                            .sizeDp(18))
                    .text("使用条例和隐私政策")
                    .subTextHtml(
                            "" +
                                    "" +
                                    "   <div class=\"jumbotron\">" +
                                    "\t\t<h5>本应用尊重并保护所有使用服务用户的个人隐私权。为了给您提供更准确、更有个性化的服务，本应用会按照本隐私权政策的规定使用和披露您的个人信息。但本应用将以高度的勤勉、审慎义务对待这些信息。除本隐私权政策另有规定外，在未征得您事先许可的情况下，本应用不会将这些信息对外披露或向第三方提供。本应用会不时更新本隐私权政策。 您在同意本应用服务使用协议之时，即视为您已经同意本隐私权政策全部内容。本隐私权政策属于本应用服务使用协议不可分割的一部分。</h5>" +
                                    "            <h4 id=\"section-1\">适用范围</h4>" +
                                    "\t <p>(a) 在您注册本应用帐号时，您根据本应用要求提供的个人注册信息；</p>" +
                                    "\t <p>(b) 在您使用本应用网络服务，或访问本应用平台网页时，本应用自动接收并记录的您手机上的信息，包括但不限于您的IP地址、使用的语言、访问日期和时间、软硬件特征信息及您需求的网页记录等数据；</p>" +
                                    "\t <p>(c) 本应用通过合法途径从商业伙伴处取得的用户个人数据。</p>" +
                                    "\t <h4>您了解并同意，以下信息不适用本隐私权政策：</h4>" +
                                    "\t <p>(a) 您在使用本应用平台提供的搜索服务时输入的关键字信息；</p>" +
                                    "\t <p>(b) 本应用收集到的您在本应用发布的有关信息数据，包括但不限于参与活动、成交信息及评价详情；</p>" +
                                    "" +
                                    "\t <p>(c) 违反法律规定或违反本应用规则行为及本应用已对您采取的措施。</p>" +
                                    "            <hr>" +
                                    "            <h4 id=\"section-2\">信息使用</h4>" +
                                    "            <p>(a)本应用不会向任何无关第三方提供、出售、出租、分享或交易您的个人信息，除非事先得到您的许可，或该第三方和本应用（含本应用关联应用）单独或共同为您提供服务，且在该服务结束后，其将被禁止访问包括其以前能够访问的所有这些资料。</p>" +
                                    "" +
                                    "\t\t <p>(b) 本应用亦不允许任何第三方以任何手段收集、编辑、出售或者无偿传播您的个人信息。任何本应用平台用户如从事上述活动，一经发现，本应用有权立即终止与该用户的服务协议。</p>" +
                                    "" +
                                    "\t\t <p>(c) 为服务用户的目的，本应用可能通过使用您的个人信息，向您提供您感兴趣的信息，包括但不限于向您发出产品和服务信息，或者与本应用合作伙伴共享信息以便他们向您发送有关其产品和服务的信息（后者需要您的事先同意）。</p>" +
                                    "" +
                                    "\t" +
                                    "            <hr>" +
                                    "            <h4 id=\"section-3\">信息披露</h4>" +
                                    "\t <h4>在如下情况下，本应用将依据您的个人意愿或法律的规定全部或部分的披露您的个人信息：</h4>" +
                                    "" +
                                    "\t <p>(a) 经您事先同意，向第三方披露；</p>" +
                                    "" +
                                    "\t <p>(b)为提供您所要求的产品和服务，而必须和第三方分享您的个人信息；</p>" +
                                    "" +
                                    "\t <p>(c)根据法律的有关规定，或者行政或司法机构的要求，向第三方或者行政、司法机构披露；</p>" +
                                    "" +
                                    "\t <p>(d) 如您出现违反中国有关法律、法规或者本应用服务协议或相关规则的情况，需要向第三方披露；</p>" +
                                    "" +
                                    "\t <p>(e) 如您是适格的知识产权投诉人并已提起投诉，应被投诉人要求，向被投诉人披露，以便双方处理可能的权利纠纷；</p>" +
                                    "" +
                                    "\t <p>(f) 在本应用平台上创建的某一交易中，如交易任何一方履行或部分履行了交易义务并提出信息披露请求的，本应用有权决定向该用户提供其交易对方的联络方式等必要信息，以促成交易的完成或纠纷的解决。</p>" +
                                    "" +
                                    "\t <p>(g) 其它本应用根据法律、法规或者网站政策认为合适的披露。</p>" +
                                    "" +
                                    "                        <hr>" +
                                    "            <h4 id=\"section-4\">信息存储和交换</h4>" +
                                    "\t\t<p>本应用收集的有关您的信息和资料将保存在本应用及（或）其关联应用的服务器上，这些信息和资料可能传送至您所在国家、地区或本应用收集信息和资料所在地的境外并在境外被访问、存储和展示。</p>" +
                                    "                        <hr>" +
                                    "            <h4 id=\"section-5\">Cookie的使用</h4>" +
                                    "\t<p>(a) 在您未拒绝接受cookies的情况下，本应用会在您的计算机上设定或取用cookies ，以便您能登录或使用依赖于cookies的本应用平台服务或功能。本应用使用cookies可为您提供更加周到的个性化服务，包括推广服务。</p>" +
                                    "" +
                                    "\t <p>(b) 您有权选择接受或拒绝接受cookies。您可以通过修改浏览器设置的方式拒绝接受cookies。但如果您选择拒绝接受cookies，则您可能无法登录或使用依赖于cookies的本应用网络服务或功能。</p>" +
                                    "" +
                                    "\t <p>（c） 通过本应用所设cookies所取得的有关信息，将适用本政策。</p>" +
                                    "\t<hr>" +
                                    "\t<h4 id=\"section-6\">信息安全</h4>" +
                                    "\t <h4>本应用帐号均有安全保护功能，请妥善保管您的用户名及密码信息。本应用将通过对用户密码进行加密等安全措施确保您的信息不丢失，不被滥用和变造。尽管有前述安全措施，但同时也请您注意在信息网络上不存在“完善的安全措施”。</h4>" +
                                    "\t<hr>" +
                                    "\t<h4 id=\"section-7\">本隐私政策的更改</h4>" +
                                    "\t<p>(a)如果决定更改隐私政策，我们会在本政策中、本应用网站中以及我们认为适当的位置发布这些更改，以便您了解我们如何收集、使用您的个人信息，哪些人可以访问这些信息，以及在什么情况下我们会透露这些信息。</p>" +
                                    "" +
                                    "\t <p>(b)本应用保留随时修改本政策的权利，因此请经常查看。如对本政策作出重大更改，本应用会通过网站通知的形式告知。</p>" +
                                    "\t" +
                                    "\t<p>请您妥善保护自己的个人信息，仅在必要的情形下向他人提供。如您发现自己的个人信息泄密，尤其是本应用用户名及密码发生泄露，请您立即联络本应用客服，以便本应用采取相应措施。</p>" +
                                    "                   </div>" +
                                    "    </div>" +
                                    "</div>" +
                                    "</body>")
                    .setIconGravity(MaterialAboutActionItem.GRAVITY_TOP)
                    .build()
            );

            return new MaterialAboutList(appCardBuilder.build(), authorCardBuilder.build(), convenienceCardBuilder.build(), otherCardBuilder.build());
        }
}
