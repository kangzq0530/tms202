function start() {
    var qrExVal = cm.getQuestRecordEx(21002);
    if(qrExVal.equals("mo1=o;mo2=o;mo3=o")) {
        cm.setQuestRecordEx(21002, "arr0=o;mo1=o;mo2=o;mo3=o");
        cm.showAvatarOrientedEffect("Effect/OnUserEff.img/guideEffect/aranTutorial/tutorialArrow3");
    }  
    cm.dispose();
}