/**
 * Author: Tatiana Fløisbonn
 * Date: 1 February 2025
 * Description: ProductDetails component for displaying detailed information about buying price trends of a product.
 */

import { PureComponent, useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import { LineChart, Line, XAxis, YAxis, CartesianGrid, Tooltip, Legend, ResponsiveContainer } from 'recharts';
import './chartsStyle.css';

function PriceTrend({ getPriceHistoryByProductnr }) {
    // Extract the product number from the URL parameters
    const { id } = useParams();

    const [priceTrend, setPriceTrend] = useState([]);
    const [currentYear, setCurrentYear] = useState(null);
    const [pastYear, setPastYear] = useState(null);

    // Fetch the price history data when the component mounts or when the id changes
    useEffect(() => {
        getPriceHistoryByProductnr(id).then(data => {
            const { currentYear, currentYearMonths, pastYear, pastYearMonths } = data;

            setCurrentYear(currentYear);
            setPastYear(pastYear);

            const monthOrder = [
                "JANUARY", "FEBRUARY", "MARCH", "APRIL", "MAY", "JUNE",
                "JULY", "AUGUST", "SEPTEMBER", "OCTOBER", "NOVEMBER", "DECEMBER"
            ];

            // Create a set of all months from both years to ensure we have all months represented
            const monthsSet = new Set([
                ...Object.keys(currentYearMonths || {}),
                ...Object.keys(pastYearMonths || {})
            ]);
            // Convert the set to an array
            const months = Array.from(monthsSet);

            // Sort months in chronological order
            months.sort((a, b) => monthOrder.indexOf(a) - monthOrder.indexOf(b));

            // Abbreviate month names to first 3 letters and uppercase
            const chartData = months.map(month => ({
                month: month.slice(0, 3).toUpperCase(),
                price_trend_current_year: currentYearMonths?.[month] ?? null,
                price_trend_past_year: pastYearMonths?.[month] ?? null
            }));

            setPriceTrend(chartData);
        });
    }, [id, getPriceHistoryByProductnr]);

    // Custom tick components for X and Y axes
    class CustomizedXAxisTick extends PureComponent {
        render() {
            // This component renders the month names on the X-axis
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

    // The CustomizedYAxisTick component renders the Y-axis labels
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

    // Custom tooltip component to display detailed information on hover
    function CustomTooltip({ payload, label, active }) {
        if (active && payload && payload.length) {
            return (
                <div className="custom-tooltip">
                    <p className="label">{label}</p>
                    {payload.map((entry, index) => (
                        <p key={`item-${index}`} className="label">
                            {entry.name}: {entry.value}
                        </p>
                    ))}
                </div>
            );
        }
        return null;
    }

    return (
        <>
            <div className='chart' style={{ width: '100%', height: "20rem" }}>
                <h4>
                    Monthly Posts
                    {currentYear && pastYear && (
                        <span style={{ marginLeft: 16, fontWeight: 'normal', fontSize: '1rem' }}>
                            ({pastYear} vs {currentYear})
                        </span>
                    )}
                </h4>
                <ResponsiveContainer width="100%">
                    <LineChart data={priceTrend}>
                        <Legend verticalAlign="top" height="3rem" align='left' />
                        <XAxis axisLine={false} dataKey="month" tick={<CustomizedXAxisTick />} tickLine={false} />
                        <YAxis axisLine={false} tick={<CustomizedYAxisTick />} tickLine={false} />
                        <Tooltip content={<CustomTooltip />} />
                        <CartesianGrid vertical={false} stroke="#D8D8D8" />
                        <Line connectNulls legendType='circle' name={currentYear ? `Current Year (${currentYear})` : 'Current Year'} type="monotone" dataKey="price_trend_current_year" stroke="var(--highlight-color)" filterNull='true' dot={{ fill: 'var(--highlight-color)' }} />
                        <Line connectNulls legendType='circle' name={pastYear ? `Past Year (${pastYear})` : 'Past Year'} type="monotone" dataKey="price_trend_past_year" stroke="var(--chart-subline-color)" filterNull='true' dot={{ fill: "var(--chart-subline-color)" }} />
                    </LineChart>
                </ResponsiveContainer>
            </div>
        </>
    );
}

export default PriceTrend;