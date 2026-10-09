import { useState, useEffect } from "react";
import { invoiceApi } from "./services/apiServices.js";
import StatusBadge from "./StatusBadge.jsx";

const Invoices = () => {
  const [invoices, setInvoices] = useState([]);
  const [selectedInvoice, setSelectedInvoice] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const role = localStorage.getItem("role");
  const isAdmin = role === "ADMIN";

  useEffect(() => {
    const fetchInvoices = async () => {
      setLoading(true);
      try {
        const res = isAdmin ? await invoiceApi.getAll() : await invoiceApi.getMyInvoices();
        setInvoices(res.data);
        setError("");
      } catch (err) {
        setError("Failed to load invoice records.");
      } finally {
        setLoading(false);
      }
    };

    fetchInvoices();
  }, [isAdmin]);

  const handlePrint = () => {
    window.print();
  };

  if (loading) {
    return <div style={{ color: "var(--text-muted)", padding: "20px 0" }}>Loading invoices...</div>;
  }

  return (
    <div>
      <h2 className="page-title">{isAdmin ? "All Issued Invoices" : "My Invoices & Receipts"}</h2>

      {error && <div className="error-text">{error}</div>}

      <div className="card">
        <div className="table-wrapper">
          <table>
            <thead>
              <tr>
                <th>Invoice #</th>
                {isAdmin && <th>Member</th>}
                <th>Plan</th>
                <th>Issue Date</th>
                <th>Subtotal</th>
                <th>Discount</th>
                <th>Total Paid</th>
                <th>Status</th>
                <th style={{ textAlign: "right" }}>Action</th>
              </tr>
            </thead>
            <tbody>
              {invoices.length === 0 ? (
                <tr>
                  <td colSpan={isAdmin ? "9" : "8"} className="empty-state">
                    No invoices generated yet.
                  </td>
                </tr>
              ) : (
                invoices.map((inv) => (
                  <tr key={inv.id}>
                    <td style={{ fontWeight: "bold", color: "var(--accent)" }}>{inv.invoiceNumber}</td>
                    {isAdmin && <td>{inv.memberName} ({inv.memberEmail})</td>}
                    <td>{inv.planName}</td>
                    <td>{inv.issueDate}</td>
                    <td>Rs. {inv.subtotal}</td>
                    <td style={{ color: "var(--success)" }}>- Rs. {inv.discount}</td>
                    <td style={{ fontWeight: "bold" }}>Rs. {inv.finalAmount}</td>
                    <td>
                      <StatusBadge status={inv.status} />
                    </td>
                    <td style={{ textAlign: "right" }}>
                      <button
                        onClick={() => setSelectedInvoice(inv)}
                        className="btn btn-gray btn-sm"
                      >
                        View Receipt
                      </button>
                    </td>
                  </tr>
                ))
              )}
            </tbody>
          </table>
        </div>
      </div>

      {/* Invoice Modal / Receipt View */}
      {selectedInvoice && (
        <div className="modal-backdrop">
          <div className="card" style={{ width: "550px", maxWidth: "95%", backgroundColor: "#171717" }}>
            <div style={{ display: "flex", justifyContent: "space-between", alignItems: "center", borderBottom: "2px solid var(--accent)", paddingBottom: "12px", marginBottom: "16px" }}>
              <div>
                <h3 style={{ margin: 0, color: "var(--accent)" }}>GYM MANAGER RECEIPT</h3>
                <span style={{ fontSize: "12px", color: "var(--text-muted)" }}>Official Membership Tax Invoice</span>
              </div>
              <StatusBadge status={selectedInvoice.status} />
            </div>

            <div style={{ display: "grid", gridTemplateColumns: "1fr 1fr", gap: "12px", fontSize: "14px", marginBottom: "16px" }}>
              <div>
                <div style={{ color: "var(--text-muted)", fontSize: "12px" }}>INVOICE TO:</div>
                <div style={{ fontWeight: "bold" }}>{selectedInvoice.memberName}</div>
                <div style={{ color: "var(--text-muted)", fontSize: "13px" }}>{selectedInvoice.memberEmail}</div>
                <div style={{ color: "var(--text-muted)", fontSize: "13px" }}>Phone: {selectedInvoice.memberPhone || "N/A"}</div>
              </div>
              <div style={{ textAlign: "right" }}>
                <div style={{ color: "var(--text-muted)", fontSize: "12px" }}>INVOICE DETAILS:</div>
                <div style={{ fontWeight: "bold", color: "var(--accent)" }}>{selectedInvoice.invoiceNumber}</div>
                <div>Date: {selectedInvoice.issueDate}</div>
                <div>Txn ID: {selectedInvoice.transactionId || "N/A"}</div>
              </div>
            </div>

            {selectedInvoice.branchName && (
              <div style={{ background: "var(--surface-2)", padding: "10px", borderRadius: "6px", fontSize: "13px", marginBottom: "16px" }}>
                📍 <strong>Branch Location:</strong> {selectedInvoice.branchName} ({selectedInvoice.branchCity || ""})
              </div>
            )}

            {/* Item Table */}
            <table style={{ marginBottom: "16px" }}>
              <thead>
                <tr>
                  <th>Description</th>
                  <th style={{ textAlign: "right" }}>Amount</th>
                </tr>
              </thead>
              <tbody>
                <tr>
                  <td>{selectedInvoice.planName} Membership Subscription</td>
                  <td style={{ textAlign: "right" }}>Rs. {selectedInvoice.subtotal}</td>
                </tr>
                {selectedInvoice.discount > 0 && (
                  <tr style={{ color: "var(--success)" }}>
                    <td>Promotional Discount</td>
                    <td style={{ textAlign: "right" }}>- Rs. {selectedInvoice.discount}</td>
                  </tr>
                )}
                {selectedInvoice.taxAmount > 0 && (
                  <tr>
                    <td>GST / Tax (5% Incl.)</td>
                    <td style={{ textAlign: "right" }}>Rs. {selectedInvoice.taxAmount}</td>
                  </tr>
                )}
                <tr style={{ borderTop: "2px solid var(--border)", fontWeight: "bold", fontSize: "16px" }}>
                  <td>Grand Total Paid</td>
                  <td style={{ textAlign: "right", color: "var(--accent)" }}>Rs. {selectedInvoice.finalAmount}</td>
                </tr>
              </tbody>
            </table>

            <div style={{ display: "flex", justifyContent: "space-between", alignItems: "center", borderTop: "1px solid var(--border)", paddingTop: "14px" }}>
              <span style={{ fontSize: "12px", color: "var(--text-muted)" }}>
                Paid via: {selectedInvoice.paymentMethod || "UPI"}
              </span>
              <div style={{ display: "flex", gap: "8px" }}>
                <button onClick={handlePrint} className="btn btn-sm">
                  🖨️ Print
                </button>
                <button onClick={() => setSelectedInvoice(null)} className="btn btn-gray btn-sm">
                  Close
                </button>
              </div>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};

export default Invoices;
