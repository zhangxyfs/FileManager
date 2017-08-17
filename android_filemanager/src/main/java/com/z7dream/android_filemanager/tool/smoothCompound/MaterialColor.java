package com.z7dream.android_filemanager.tool.smoothCompound;


import com.z7dream.android_filemanager.R;
import com.z7dream.android_filemanager.base.mvp.BaseAppli;

public class MaterialColor {

    public static final int material_deep_teal_500 = 0xff009688;

    //搬运自themes_material.java中的Theme.Material.Light
    public static final class DefaultLight {
        //我是分割线
        public static final int colorPrimaryDark = BaseAppli.getContext().getResources().getColor(R.color.colorPrimaryDark);//0xff757575;//@color/primary_dark_material_light
        public static final int colorPrimary = BaseAppli.getContext().getResources().getColor(R.color.colorPrimary);//0xffbdbdbd;//@color/primary_material_light
        public static final int colorAccent = BaseAppli.getContext().getResources().getColor(R.color.color_white);//material_deep_teal_500;//@color/accent_material_light
        //我是分割线
        //public static final int colorControlNormal = 0;//?attr/textColorSecondary
        //public static final int colorControlActivated = 0;//?attr/colorAccent
        //我是分割线
        public static final int colorControlHighlight = 0x40000000;//@color/ripple_material_light
        //public static final int colorButtonNormal = 0;//@color/btn_default_material_light
        //public static final int colorSwitchThumbNormal = 0;//@color/switch_thumb_material_light

    }

}
