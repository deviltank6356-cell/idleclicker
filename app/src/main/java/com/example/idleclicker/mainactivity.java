package com.example.idleclicker;

import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.text.NumberFormat;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private double money = 0.0;
    private double clickValue = 1.0;
    private int autoClickerCount = 0;
    private double autoClickerRate = 0.0;

    private double clickUpgradeCost = 10.0;
    private double autoClickerCost = 50.0;

    private TextView moneyText;
    private Button clickButton;
    private Button clickUpgradeButton;
    private Button autoClickerButton;
    private TextView statsText;

    private NumberFormat numberFormat;
    private Handler handler;
    private Runnable autoClickerRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        numberFormat = NumberFormat.getNumberInstance(Locale.US);
        handler = new Handler();

        moneyText = findViewById(R.id.moneyText);
        clickButton = findViewById(R.id.clickButton);
        clickUpgradeButton = findViewById(R.id.clickUpgradeButton);
        autoClickerButton = findViewById(R.id.autoClickerButton);
        statsText = findViewById(R.id.statsText);

        clickButton.setOnClickListener(v -> addMoney(clickValue));

        clickUpgradeButton.setOnClickListener(v -> buyClickUpgrade());

        autoClickerButton.setOnClickListener(v -> buyAutoClicker());

        startAutoClicker();

        updateUI();
    }

    private void addMoney(double amount) {
        money += amount;
        updateUI();
    }

    private void buyClickUpgrade() {
        if (money >= clickUpgradeCost) {
            money -= clickUpgradeCost;
            clickValue += 1.0;
            clickUpgradeCost *= 1.5;
            updateUI();
        }
    }

    private void buyAutoClicker() {
        if (money >= autoClickerCost) {
            money -= autoClickerCost;
            autoClickerCount++;
            autoClickerRate += 1.0;
            autoClickerCost *= 1.8;
            updateUI();
        }
    }

    private void startAutoClicker() {
        autoClickerRunnable = new Runnable() {
            @Override
            public void run() {
                if (autoClickerRate > 0) {
                    addMoney(autoClickerRate);
                }
                handler.postDelayed(this, 1000);
            }
        };
        handler.post(autoClickerRunnable);
    }

    private void updateUI() {
        moneyText.setText("Money: $" + numberFormat.format(money));

        clickUpgradeButton.setText("Upgrade Click\n+$1 per click\nCost: $" +
                numberFormat.format(clickUpgradeCost));
        clickUpgradeButton.setEnabled(money >= clickUpgradeCost);

        autoClickerButton.setText("Buy Auto-Clicker\n+$1/sec\nCost: $" +
                numberFormat.format(autoClickerCost));
        autoClickerButton.setEnabled(money >= autoClickerCost);

        statsText.setText("Click Value: $" + numberFormat.format(clickValue) + "\n" +
                "Auto-Clickers: " + autoClickerCount + "\n" +
                "Income: $" + numberFormat.format(autoClickerRate) + "/sec");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(autoClickerRunnable);
    }
}