
function start() {
    var qRValue = cm.getQuestRecordEx(21002);
    if (qRValue.equals("mo1=o")) {
        cm.setQuestRecordEx(21002, "mo1=o;mo2=o");
        cm.showAvatarOrientedEffect("Effect/OnUserEff.img/guideEffect/aranTutorial/legendBalloon2");
	}
	cm.dispose();
}