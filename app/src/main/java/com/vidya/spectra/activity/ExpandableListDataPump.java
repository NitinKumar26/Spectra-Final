package com.vidya.spectra.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

class ExpandableListDataPump {
    public static HashMap<String, List<String>> getData() {
        HashMap<String, List<String>> expandableListDetail = new HashMap<>();

        List<String> rules = new ArrayList<>();
        rules.add("1. Speech must have been previously delivered in public by any famous personalities or characters (Politicians, actors or speeches in movies etc.).\n\n" +
                "2. Prior information should be given to the coordinators up to 26 Jan.2018 on mail id given below (link of YouTube, title of speech etc.)\n\n" +
                "3. Negative points for Paper reading.\n\n" +
                "4. Speeches are to be max ten minutes in length.\n\n" +
                "5. Imitation of the original speaker should be accurate with the same notion.\n");


        List<String> judgment = new ArrayList<>();
        judgment.add("1. Presentation (10)\n\n"+"2. Body Language (10)\n\n"+"3. Costume (10)\n\n"+"4. Fluency (10)\n\n"+"5. Imitation (10)\n" );

        expandableListDetail.put("JUDGMENT CRITERIA", judgment);
        expandableListDetail.put("RULES & REGULATIONS", rules);
        return expandableListDetail;
    }
}
