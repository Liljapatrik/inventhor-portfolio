import React, { PureComponent } from 'react';
import { LineChart, Line, XAxis, YAxis, CartesianGrid, Tooltip, Legend, ResponsiveContainer } from 'recharts';
import './chartsStyle.css';

/**
 * @Author Steewen Dennis Chanavi Holden
 *
 * Renders a line chart comparing monthly sales for the current and previous year.
 *
 * Receives sales data, transforms it for chart display.
 * Uses recharts for rendering the chart.
 * Displays lines for both the current year and the previous year.
 *
 * Styling is applied via 'chartsStyle.css'.
 */

function TotalSalesChart({ salesData }) {
    // Transform the data from backend to match the chart format
    const transformedData = [];
    const currentYear = new Date().getFullYear();
    const months = ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'];

    if (salesData && salesData.length > 0) {
        // For each month, collect current and past year sales
        months.forEach((month, index) => {
            const currentYearData = salesData.find(d => d.year === currentYear && d.monthNumber === index + 1);
            const prevYearData = salesData.find(d => d.year === currentYear - 1 && d.monthNumber === index + 1);

            transformedData.push({
                month: month,
                amount_money_current_year: currentYearData ? currentYearData.totalSales : 0,
                amount_money_past_year: prevYearData ? prevYearData.totalSales : 0
            });
        });
    }

    // X axis (months)
    class CustomizedXAxisTick extends PureComponent {
        render() {
            const { x, y, stroke, payload } = this.props;

            return (
                <g transform={`translate(${x},${y})`}>
                    <text x={0} y={6} dy={16} textAnchor="middle" fill="var(--chart-title-color)" transform="rotate(0)">
                        {payload.value}
                    </text>
                </g>
            );
        }
    }

    // Y axis (amounts)
    class CustomizedYAxisTick extends PureComponent {
        render() {
            const { x, y, stroke, payload } = this.props;

            return (
                <g transform={`translate(${x},${y})`}>
                    <text x={-20} y={0} dy={6} textAnchor="middle" fill="var(--chart-title-color)" transform="rotate(0)">
                        {payload.value}
                    </text>
                </g>
            );
        }
    }

    // Custom tooltip for showing values when hovering chart points
    function CustomTooltip({ payload, label, active }) {
        if (active && payload && payload.length) {
            return (
                <div className="custom-tooltip">
                    {payload.map((entry, index) => (
                        <p key={`item-${index}`} className="label">{`${label} : ${entry.value}`}</p>
                    ))}
                </div>
            );
        }
        return null;
    }

    return (
        <div className='chart' style={{ width: '100%', height: "20rem" }}>
            <h4>Monthly Sales</h4>
            <ResponsiveContainer width="100%">
                <LineChart data={transformedData}>
                    {/* Chart legend at the top left */}
                    <Legend verticalAlign="top" height="3rem" align='left' />
                    {/* X axis: Months */}
                    <XAxis axisLine={false} dataKey="month" tick={<CustomizedXAxisTick />} tickLine={false} />
                    {/* Y axis: Sales values */}
                    <YAxis axisLine={false} tick={<CustomizedYAxisTick />} tickLine={false} />
                    {/* Custom tooltip */}
                    <Tooltip content={<CustomTooltip />}/>
                    {/* Horizontal grid lines */}
                    <CartesianGrid vertical={false} stroke="#D8D8D8" />
                    {/* Line for current year sales */}
                    <Line connectNulls legendType='circle' name='Current Year' type="monotone" dataKey="amount_money_current_year" stroke="var(--highlight-color)" filterNull='true' dot={{ fill:'var(--highlight-color)' }}  />
                    {/* Line for previous year sales */}
                    <Line connectNulls legendType='circle' name='Past Year' type="monotone" dataKey="amount_money_past_year" stroke="var(--chart-subline-color)" filterNull='true' dot={{fill:"var(--chart-subline-color)"}}  />
                </LineChart>
            </ResponsiveContainer>
        </div>
    );
}

export default TotalSalesChart;