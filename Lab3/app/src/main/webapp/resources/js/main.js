$(function () {
    const X_VALUES = [-2.0, -1.5, -1.0, -0.5, 0.0, 0.5, 1.0, 1.5, 2.0];
    const Y_MIN = -5, Y_MAX = 5;
    const R_VALUES = [1, 2, 3, 4, 5];

    let xVal;
    let yVal;
    let rVal;

    const canone = 68;

    let canvasCurrent = $('.graph-canvas-current');
    let canvasPoints = $('.graph-canvas-points');
    let info = $('.input-form-info');

    function isNumberic(x) {
        return !isNaN(parseFloat(x)) && isFinite(x);
    }

    function validateX() {
        xVal = $('.input-form-select-x option:selected').val();

        if (isNumberic(xVal) && X_VALUES.includes(parseFloat(xVal))) {
            info.text('Enter all coordinates');
            return true;
        } else {
            info.text("Choose X");
            return false;
        }
    }

    function validateY() {
        yVal = $('.input-form-text-y').val();

        if (isNumberic(yVal) && yVal >= Y_MIN && yVal <= Y_MAX) {
            info.text("Enter all coordinates");
            return true;
        } else {
            info.text(`Enter Y from ${Y_MIN} to ${Y_MAX}`);
            return false;
        }
    }

    function validateR() {
        if (isNumberic(rVal) && R_VALUES.includes(parseFloat(rVal))) {
            info.text("Enter all coordinates");
            return true;
        } else {
            info.text("Choose R");
            return false;
        }
    }

    function validateForm() {
        return validateX() && validateY() && validateR();
    }

    function drawTablePoint(x, y, r, hitResult) {
        let ctxPoints = canvasPoints[0].getContext('2d');
        ctxPoints.fillStyle = hitResult === 'miss' ? 'red' : 'green';
        ctxPoints.beginPath();
        ctxPoints.arc(
            x / r * canone + canvasPoints.width() / 2,
            -y / r * canone + canvasPoints.height() / 2,
            2, 0, 2 * Math.PI);
        ctxPoints.fill();
    }

    function loadTablePoints() {
        let rows = [];
        let headers = $(".result-table th");

        $(".result-table tr").each(function (index) {
            let cells = $(this).find("td");
            rows[index] = {};
            cells.each(function (cellIndex) {
                rows[index][$(headers[cellIndex]).html()] = $(this).html().replace(/\s/g, "");
            });
        });
        for (let i = 0; i < rows.length; i++) {
            drawTablePoint(
                rows[i]['X'],
                rows[i]['Y'],
                rows[i]['R'],
                rows[i]['Result']
            );
        }
    }

    function clearCanvasCurrent() {
        canvasCurrent[0].getContext('2d').clearRect(0, 0, canvasCurrent.width(), canvasCurrent.height());
    }

    function drawCurrentPoint(x, y) {
        clearCanvasCurrent();
        if (x > canvasCurrent.width() || x < 0 || y > canvasCurrent.height() || y < 0) return;

        let ctxAxes = canvasCurrent[0].getContext('2d');
        ctxAxes.setLineDash([2, 2]);
        ctxAxes.fillStyle = 'black';
        ctxAxes.beginPath();
        ctxAxes.moveTo(x, canvasCurrent.width() / 2);
        ctxAxes.lineTo(x, y);
        ctxAxes.moveTo(canvasCurrent.height() / 2, y);
        ctxAxes.lineTo(x, y);
        ctxAxes.stroke();
        ctxAxes.arc(x, y, 2, 0, Math.PI);
        ctxAxes.fill();
    }

    function redrawCurrentFromInput() {
        if (validateForm()) {
            drawCurrentPoint(xVal * canone / rVal + canvasCurrent.width() / 2, -(yVal / rVal * canone - canvasCurrent.height() / 2));
        } else {
            clearCanvasCurrent();
        }
    }

    canvasCurrent.on('click', function (event) {
        if (!validateR()) return;

        let canvasX = (event.offsetX - canvasCurrent.width() / 2) / canone * rVal;
        let minDiff = Infinity;
        let nearestX;

        for (let i = 0; i < X_VALUES.length; i++) {
            if (Math.abs(canvasX - X_VALUES[i]) < minDiff) {
                minDiff = Math.abs(canvasX - X_VALUES[i]);
                nearestX = X_VALUES[i];
            }
        }

        let canvasY = (-event.offsetY + canvasCurrent.height() / 2) / canone * rVal;
        if (canvasY < Y_MIN) {
            canvasY = Y_MIN;
        } else if(canvasY > Y_MAX) {
            canvasY = Y_MAX;
        }

        drawCurrentPoint(nearestX * canone / rVal + canvasCurrent.width() / 2, -(canvasY/rVal * canone - canvasCurrent.height() / 2));
        let selectedX = $('.input-form-select-x option[value="' + nearestX +'"]');
        selectedX.prop('selected', true);

        $('.input-form-select-x option').not(selectedX).prop('selected', false);
        $('.input-form-text-y').val(canvasY.toString().substring(0,10));
    });

    $('.input-form-select-x').on('change', event => redrawCurrentFromInput());
    $('.input-form-text-y').on('input', event => redrawCurrentFromInput());
    $('.input-form-button-r').on('click', function (event) {
        rVal = $(this).val();
        if(!validateR) return;

        $(this).addClass('input-form-button-r-clicked');
        $('.input-form-button-r').not(this).removeClass('input-form-button-r-clicked');

        let svgGraph = document.querySelector(".result-graph").getSVGDocument();
        svgGraph.querySelector('.coordinate-text_minus-Rx').textContent = (-rVal).toString();
        svgGraph.querySelector('.coordinate-text_minus-Ry').textContent = (-rVal).toString();
        svgGraph.querySelector('.coordinate-text_minus-half-Rx').textContent = (-rVal/2).toString();
        svgGraph.querySelector('.coordinate-text_minus-half-Ry').textContent = (-rVal/2).toString();
        svgGraph.querySelector('.coordinate-text_plus-Rx').textContent = (rVal).toString();
        svgGraph.querySelector('.coordinate-text_plus-Ry').textContent = (rVal).toString();
        svgGraph.querySelector('.coordinate-text_plus-half-Rx').textContent = (rVal/2).toString();
        svgGraph.querySelector('.coordinate-text_plus-half-Ry').textContent = (rVal/2).toString();

        redrawCurrentFromInput();
    });

    $('.input-form-control-buttons-button-submit').on('click', function (event) {
        if(!validateForm()) {
            event.preventDefault();
        } else {
            $('.input-form-hidden-r input[type=hidden]').val(rVal);
        }
    });
    loadTablePoints();
});