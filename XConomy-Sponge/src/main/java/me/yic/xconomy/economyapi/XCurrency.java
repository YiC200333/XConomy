package me.yic.xconomy.economyapi;

import me.yic.xconomy.XConomy;
import org.spongepowered.api.service.economy.Currency;
import org.spongepowered.api.text.Text;

import java.math.BigDecimal;

public class XCurrency implements Currency {

    @Override
    public Text getDisplayName() {
        return Text.of(XConomy.config.getNode("Currency","singular-name").getString());
    }

    @Override
    public Text getPluralDisplayName() {
        return Text.of(XConomy.config.getNode("Currency","plural-name").getString());
    }

    @Override
    public Text getSymbol() {
        return Text.of(XConomy.config.getNode("Currency","thousands-separator").getString());
    }

    @Override
    public Text format(BigDecimal amount, int numFractionDigits) {
        return null;
       // return Text.of(DataFormat.formatBigDecimal(amount));
    }

    @Override
    public int getDefaultFractionDigits() {
        //if (DataFormat.isInteger) {
         //   return 0;
       // }
        //return 2;
        return 0;
    }

    @Override
    public boolean isDefault() {
        return true;
    }

    @Override
    public String getId() {
        return "xconomy";
    }

    @Override
    public String getName() {
        return "XConomy";
    }
}
