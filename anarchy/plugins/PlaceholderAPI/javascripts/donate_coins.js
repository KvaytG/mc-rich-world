
var placeholder = '%richdonate_coins%'

function placeholderCheck() {
	if (parseInt(placeholder) <= 0) {
		return '&c' + placeholder;
	} else {
		return '&#FFD419' + placeholder;
	}
}

placeholderCheck();
