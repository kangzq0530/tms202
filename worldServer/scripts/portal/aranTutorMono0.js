function start() {
    var qRValue = cm.getQuestRecordEx(21002);
    if (qRValue.equals("")) {
        cm.setQuestRecordEx(21002, "mo1=o");
        cm.showAvatarOrientedEffect("Effect/OnUserEff.img/guideEffect/aranTutorial/legendBalloon1");
    }
    cm.dispose();
}