
var placeholder = '%richdonate_status%'

function isOp() {
    return "%player_is_op%" === "yes";
}

function placeholderCheck() {
	if (isOp() || placeholder == 'admin') {
		return '&cАдмин';
	} else if (placeholder == 'vip') {
		return '&aVIP';
	} else {
		return '&#FFD419Нет';
	}
}

placeholderCheck();
