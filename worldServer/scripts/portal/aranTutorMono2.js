function start() {
    var qRValue = cm.getQuestRecordEx(21002);
    if (qRValue.equals("mo1=o;mo2=o")) {
        cm.setQuestRecordEx(21002, "mo1=o;mo2=o;mo3=o");
        cm.showAvatarOrientedEffect("Effect/OnUserEff.img/guideEffect/aranTutorial/legendBalloon3");
    }
    cm.dispose();
}