package hr.ferit.bozidarkelava.cashregister.singleton

object ItemDimensions {

    private var cellWidth: Double = 0.0

    private var cellHeight: Double = 0.0

    fun getCellWidth(): Double {
        return this.cellWidth
    }

    fun setCellWidth(width: Double) {
        this.cellWidth=width
    }

    fun getCellHeight(): Double {
        return this.cellHeight
    }

    fun setCellHeight(height: Double) {
        this.cellHeight=height
    }
}