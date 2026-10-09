const StatusBadge = ({ status }) => {
  const getBadgeClass = (s) => {
    switch (s) {
      case "ACTIVE":
      case "PAID":
      case "RESOLVED":
        return "badge badge-active";
      case "EXPIRING_SOON":
      case "PENDING":
      case "IN_PROGRESS":
        return "badge badge-expiring";
      case "EXPIRED":
      case "CANCELLED":
      case "CLOSED":
      case "FAILED":
      case "INACTIVE":
        return "badge badge-expired";
      case "FROZEN":
      case "REFUNDED":
        return "badge badge-frozen";
      default:
        return "badge";
    }
  };

  return <span className={getBadgeClass(status)}>{status || "N/A"}</span>;
};

export default StatusBadge;
