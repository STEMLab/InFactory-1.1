package edu.pnu.stem.binder;

import java.math.BigDecimal;
import javax.xml.bind.annotation.adapters.XmlAdapter;

public class AdapterForDouble extends XmlAdapter<BigDecimal, Double> {

	@Override
	public Double unmarshal(BigDecimal v) {
		return new Double(v.toString());
	}

	@Override
	public BigDecimal marshal(Double v) {
		return new BigDecimal(v);
	}
}
