package com.lambadam.tuna.poecurrency.elements;

import com.lambadam.tuna.poecurrency.R;

public final class ItemProperties {
    public static final String [] itemNames = {"Orb of Alteration","Orb of Fusing","Orb of Alchemy",
            "Chaos Orb","Gemcutters Prism","Exalted Orb","Chromatic Orb",
            "Jewellers Orb","Orb of Chance","Cartographers Chisel",
            "Orb of Scouring","Blessed Orb","Orb of Regret","Regal Orb",
            "Divine Orb","Vaal Orb","Scroll of Wisdom","Portal Scroll",
            "Armourers Scrap","Blacksmiths Whetstone","Glassblowers Bauble",
            "Orb of Transmutation","Orb of Augmentation","Mirror of Kalandra",
            "Eternal Orb","Perandus Coin","Silver Coin"};
    public static final int [] itemIds = {1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18
            ,19,20,21,22,23,24,25,26,35};
    public static final int [] itemImages = {
            R.drawable.orb_of_alteration_inventory_icon, R.drawable.orb_of_fusing_inventory_icon,
            R.drawable.orb_of_alchemy_inventory_icon,R.drawable.chaos_orb_inventory_icon,
            R.drawable.gemcutters_prism_inventory_icon,R.drawable.exalted_orb_inventory_icon,
            R.drawable.chromatic_orb_inventory_icon,R.drawable.jewellers_orb_inventory_icon,
            R.drawable.orb_of_chance_inventory_icon,R.drawable.cartographers_chisel_inventory_icon,
            R.drawable.orb_of_scouring_inventory_icon,R.drawable.blessed_orb_inventory_icon,
            R.drawable.orb_of_regret_inventory_icon,R.drawable.regal_orb_inventory_icon,
            R.drawable.divine_orb_inventory_icon,R.drawable.vaal_orb_inventory_icon,
            R.drawable.scroll_of_wisdom_inventory_icon,R.drawable.portal_scroll_inventory_icon,
            R.drawable.armourers_scrap_inventory_icon,R.drawable.blacksmiths_whetstone_inventory_icon,
            R.drawable.glassblowers_bauble_inventory_icon,R.drawable.orb_of_transmutation_inventory_icon,
            R.drawable.orb_of_augmentation_inventory_icon,R.drawable.mirror_of_kalandra_inventory_icon,
            R.drawable.eternal_orb_inventory_icon,R.drawable.perandus_coin_inventory_icon,R.drawable.silver_coin_inventory_icon
    };

    public static final String [] leagues = {
            "Betrayal","Hardcore Betrayal","Standard","Hardcore"
    };

    public static int getPosition(int id) {
        for(int i = 0;i<itemIds.length;i++) {
            if(itemIds[i] == id) return i;
        }
        return -1;
    }
}
