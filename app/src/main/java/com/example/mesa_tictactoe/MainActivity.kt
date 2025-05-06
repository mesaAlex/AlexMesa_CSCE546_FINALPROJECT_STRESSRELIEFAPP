package com.example.mesa_tictactoe

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.GridLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var buttons: Array<Array<Button>>
    private lateinit var statusText: TextView
    private lateinit var seeMemeButton: Button
    private var board = Array(3) { Array(3) { ' ' } }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        statusText = findViewById(R.id.statusText)
        seeMemeButton = findViewById(R.id.seeMemeButton)
        seeMemeButton.visibility = View.GONE

        val restartButton = findViewById<Button>(R.id.restartButton)
        restartButton.setOnClickListener {
            restartGame()
        }

        seeMemeButton.setOnClickListener {
            val intent = Intent(this, AxolotlActivity::class.java)
            startActivity(intent)
        }

        buttons = Array(3) { row ->
            Array(3) { col ->
                val buttonId = resources.getIdentifier("button$row$col", "id", packageName)
                findViewById<Button>(buttonId).apply {
                    setOnClickListener { playerMove(row, col) }
                }
            }
        }
    }

    private fun playerMove(row: Int, col: Int) {
        if (board[row][col] != ' ') return

        board[row][col] = 'X'
        buttons[row][col].text = "X"

        if (checkWin('X')) {
            statusText.text = "You Win!"
            disableAllButtons()
            seeMemeButton.visibility = View.VISIBLE
            return
        }

        if (isBoardFull()) {
            statusText.text = "Draw!"
            return
        }

        computerMove()
    }

    private fun computerMove() {
        val emptyCells = mutableListOf<Pair<Int, Int>>()

        for (i in 0..2) {
            for (j in 0..2) {
                if (board[i][j] == ' ') {
                    emptyCells.add(Pair(i, j))
                }
            }
        }

        if (emptyCells.isNotEmpty()) {
            val (row, col) = emptyCells.random()
            board[row][col] = 'O'
            buttons[row][col].text = "O"

            if (checkWin('O')) {
                statusText.text = "Computer Wins!"
                disableAllButtons()
            } else if (isBoardFull()) {
                statusText.text = "Draw!"
            }
        }
    }

    private fun checkWin(symbol: Char): Boolean {
        for (i in 0..2) {
            if (board[i][0] == symbol && board[i][1] == symbol && board[i][2] == symbol) return true
            if (board[0][i] == symbol && board[1][i] == symbol && board[2][i] == symbol) return true
        }
        if (board[0][0] == symbol && board[1][1] == symbol && board[2][2] == symbol) return true
        if (board[0][2] == symbol && board[1][1] == symbol && board[2][0] == symbol) return true

        return false
    }

    private fun isBoardFull(): Boolean {
        for (row in board) {
            if (row.contains(' ')) return false
        }
        return true
    }

    private fun disableAllButtons() {
        for (row in buttons) {
            for (button in row) {
                button.isEnabled = false
            }
        }
    }

    private fun restartGame() {
        board = Array(3) { Array(3) { ' ' } }

        for (i in 0..2) {
            for (j in 0..2) {
                buttons[i][j].text = ""
                buttons[i][j].isEnabled = true
            }
        }

        statusText.text = "Your Turn"
        seeMemeButton.visibility = View.GONE
    }
}