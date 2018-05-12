function start() {
    var qRValue = cm.getQuestRecordEx(21002);
    if (qRValue.equals("normal=o;arr0=o;mo1=o;mo2=o;mo3=o")) {
        cm.setQuestRecordEx(21002, "normal=o;arr0=o;mo1=o;mo2=o;mo3=o;mo4=o");
        cm.showAvatarOrientedEffect("Effect/OnUserEff.img/guideEffect/aranTutorial/legendBalloon6");
    }
    cm.dispose();
}