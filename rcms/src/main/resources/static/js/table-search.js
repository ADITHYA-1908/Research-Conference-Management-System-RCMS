/**
 * RCMS Academic Data Table Real-Time Search Utility
 */
document.addEventListener('DOMContentLoaded', function () {
    document.querySelectorAll('.table-search-input').forEach(function (input) {
        var table = document.getElementById(input.getAttribute('data-table-target'));
        if (!table) return;
        input.addEventListener('keyup', function () {
            var filter = input.value.toLowerCase().trim();
            table.querySelectorAll('tbody tr').forEach(function (row) {
                if (row.querySelector('[colspan]')) return; // skip empty-state placeholder rows
                row.style.display = row.textContent.toLowerCase().includes(filter) ? '' : 'none';
            });
        });
    });
});
