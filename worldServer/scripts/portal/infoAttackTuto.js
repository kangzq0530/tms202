/* RED 1st impact
    Explorer tut
    Made by Daenerys
*/
function start() {
    if (cm.hasQuestInProgress(32205)) {
        cm.showAvatarOrientedEffect("UI/tutorial.img/21");
    }
    cm.dispose();
}