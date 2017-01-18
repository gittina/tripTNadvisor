var unique = function (origArr) {
    var newArr = [], origLen = origArr.length, found, x, y;

    for (x = 0; x < origLen; x++) {
        found = undefined;
        for (y = 0; y < newArr.length; y++) {
            if ((typeof origArr[x] !== 'string') || (typeof newArr[y] !== 'string'))
                break;
            else if (origArr[x].toLowerCase() === newArr[y].toLowerCase()) {
                found = true;
                break;
            }
        }
        if (!found) {
            newArr.push(origArr[x]);
        }
    }
    return newArr;
};

function setupAutocomplete(source) {
    $.get(source, function (data) {
        $("#autocomplete_jquery1").autocomplete({
            source: unique(data.split(","))
        });
    });
}

exports.setupAutocomplete = setupAutocomplete;
