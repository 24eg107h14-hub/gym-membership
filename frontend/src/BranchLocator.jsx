import { useMemo, useState } from "react";
import { STATE_NAMES, getCitiesForState } from "./data/indiaLocations.js";

/**
 * Location based branch finder.
 *
 * Renders a search box plus State / City filters and the matching branches so a
 * member can find the gym nearest to them. It only browses locations — a
 * membership is valid at every branch, so nothing here is selected or attached.
 */
const BranchLocator = ({
  branches = [],
  renderItem,
  emptyMessage = "No branches found for these filters.",
}) => {
  const [query, setQuery] = useState("");
  const [stateFilter, setStateFilter] = useState("");
  const [cityFilter, setCityFilter] = useState("");

  const stateOptions = useMemo(() => {
    const fromData = branches.map((b) => (b.state || "").trim()).filter(Boolean);
    return Array.from(new Set([...fromData, ...STATE_NAMES])).sort((a, b) => a.localeCompare(b));
  }, [branches]);

  const cityOptions = useMemo(() => {
    const fromData = branches
      .filter((b) => !stateFilter || (b.state || "").trim() === stateFilter)
      .map((b) => (b.city || "").trim())
      .filter(Boolean);
    const fromDataset = stateFilter ? getCitiesForState(stateFilter) : [];
    return Array.from(new Set([...fromData, ...fromDataset])).sort((a, b) => a.localeCompare(b));
  }, [branches, stateFilter]);

  const filtered = useMemo(() => {
    const q = query.trim().toLowerCase();
    return branches.filter((branch) => {
      if (stateFilter && (branch.state || "").trim() !== stateFilter) return false;
      if (cityFilter && (branch.city || "").trim() !== cityFilter) return false;
      if (!q) return true;
      return [branch.name, branch.address, branch.city, branch.state, branch.phone]
        .filter(Boolean)
        .some((value) => String(value).toLowerCase().includes(q));
    });
  }, [branches, query, stateFilter, cityFilter]);

  const hasFilters = Boolean(query.trim() || stateFilter || cityFilter);

  const clearFilters = () => {
    setQuery("");
    setStateFilter("");
    setCityFilter("");
  };

  const handleStateChange = (value) => {
    setStateFilter(value);
    setCityFilter("");
  };

  return (
    <div className="locator">
      <div className="locator-filters">
        <div className="locator-search">
          <label htmlFor="branch-search">Search by branch name or area</label>
          <input
            id="branch-search"
            type="text"
            value={query}
            onChange={(e) => setQuery(e.target.value)}
            placeholder="e.g. Bandra, Connaught Place, Fitness Hub"
          />
        </div>

        <div className="locator-selects">
          <div>
            <label htmlFor="branch-state">State</label>
            <select id="branch-state" value={stateFilter} onChange={(e) => handleStateChange(e.target.value)}>
              <option value="">All states</option>
              {stateOptions.map((stateName) => (
                <option key={stateName} value={stateName}>
                  {stateName}
                </option>
              ))}
            </select>
          </div>

          <div>
            <label htmlFor="branch-city">City</label>
            <select id="branch-city" value={cityFilter} onChange={(e) => setCityFilter(e.target.value)}>
              <option value="">All cities</option>
              {cityOptions.map((cityName) => (
                <option key={cityName} value={cityName}>
                  {cityName}
                </option>
              ))}
            </select>
          </div>
        </div>
      </div>

      <div className="locator-summary">
        <span className="locator-count">
          {filtered.length} {filtered.length === 1 ? "branch" : "branches"} found
          {cityFilter ? ` in ${cityFilter}` : stateFilter ? ` in ${stateFilter}` : " across India"}
        </span>
        {hasFilters && (
          <button type="button" className="btn btn-gray btn-sm" onClick={clearFilters}>
            Clear filters
          </button>
        )}
      </div>

      {filtered.length === 0 ? (
        <div className="locator-empty">
          {stateFilter || cityFilter || query.trim() ? `${emptyMessage} ` : "No branches available yet. "}
          {hasFilters && "Try another city or clear the filters."}
        </div>
      ) : (
        <div className="locator-results">
          {filtered.map((branch) => {
            if (renderItem) {
              return (
                <div key={branch.id} className="branch-option">
                  {renderItem(branch)}
                </div>
              );
            }

            return (
              <div key={branch.id} className="branch-option">
                <div className="branch-option-top">
                  <span className="branch-option-name">{branch.name}</span>
                  {branch.active === false && <span className="branch-inactive-pill">Inactive</span>}
                </div>
                <div className="branch-option-meta">
                  📍 {[branch.address, branch.city, branch.state].filter(Boolean).join(", ")}
                </div>
                {branch.phone && <div className="branch-option-meta">📞 {branch.phone}</div>}
                {branch.email && <div className="branch-option-meta">✉️ {branch.email}</div>}
              </div>
            );
          })}
        </div>
      )}
    </div>
  );
};

export default BranchLocator;
