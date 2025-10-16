function anyFilterActive() {
    if (savedSearch && savedSearch.length > 0) return true;
    if (savedCategory) return true;
    if (savedManufacturerId) return true;
    if (savedPowerType) return true;
    if (savedPowerMin) return true;
    if (savedPowerMax) return true;
    if (savedPriceMin) return true;
    return !!savedPriceMax;

}
function buildLoadMoreUrl() {
    let url = "/loadMore?offset=" + loadedCount + "&limit=" + limitPerLoad;
    if (savedSearch) url += "&search=" + encodeURIComponent(savedSearch);
    if (savedCategory) url += "&category=" + encodeURIComponent(savedCategory);
    if (savedManufacturerId) url += "&manufacturerId=" + savedManufacturerId;
    if (savedPowerType) url += "&powerType=" + encodeURIComponent(savedPowerType);
    if (savedPowerMin) url += "&powerMin=" + savedPowerMin;
    if (savedPowerMax) url += "&powerMax=" + savedPowerMax;
    if (savedPriceMin) url += "&priceMin=" + savedPriceMin;
    if (savedPriceMax) url += "&priceMax=" + savedPriceMax;
    return url;
}
async function loadMore() {
    if (loadedCount >= totalCount) return;
    if (anyFilterActive()) {
        document.getElementById("loadMoreBtn").style.display = "none";
        return;
    }
    let url = buildLoadMoreUrl();
    try {
        let resp = await fetch(url);
        if (!resp.ok) throw new Error("HTTP Error " + resp.status);
        let newItems = await resp.json();
        if (!Array.isArray(newItems) || newItems.length === 0) {
            document.getElementById("loadMoreBtn").style.display = "none";
            return;
        }
        loadedCount += newItems.length;
        let container = document.getElementById("applianceList");
        for (let item of newItems) {
            let colDiv = document.createElement('div');
            colDiv.className = 'col';
            colDiv.innerHTML = `
  <div class="card h-100 d-flex flex-column product-card text-decoration-none text-dark">
    <img
      class="card-img-top"
      src="https://via.placeholder.com/500x220?text=No+Image"
      alt="No Image"
      style="height: 180px; object-fit: cover;"
    />
    <div class="card-body d-flex flex-column">
      <h5 class="card-title mb-2">${item.name || 'No name'}</h5>
      <p class="card-text">${item.category || ''}</p>
      <p class="mt-auto fw-bold">$${item.formattedPrice || item.price || ''}</p>
    </div>
  </div>
`;
            container.appendChild(colDiv);
        }
        if (loadedCount >= totalCount) {
            document.getElementById("loadMoreBtn").style.display = "none";
        }
    } catch (err) {
        console.error("Load more error:", err);
    }
}
document.addEventListener("DOMContentLoaded", () => {
    if (anyFilterActive() || (totalCount <= loadedCount)) {
        let btn = document.getElementById("loadMoreBtn");
        if (btn) btn.style.display = "none";
    }
});

let searchInp = document.getElementById("searchInput");
let suggestionsDiv = document.getElementById("suggestions");
searchInp.addEventListener("input", async () => {
    let btn = document.getElementById("loadMoreBtn");
    if (btn) btn.style.display = "none";

    let query = searchInp.value.trim();
    suggestionsDiv.innerHTML = "";
    if (query.length < 1) {
        suggestionsDiv.style.display = "none";
        return;
    }
    let resp = await fetch("/autocomplete?query=" + encodeURIComponent(query));
    if (!resp.ok) return;
    let suggestions = await resp.json();
    if (!Array.isArray(suggestions) || suggestions.length === 0) {
        suggestionsDiv.style.display = "none";
        return;
    }
    suggestionsDiv.style.display = "block";
    for (let s of suggestions) {
        let div = document.createElement("div");
        div.textContent = s;
        div.addEventListener("click", () => {
            searchInp.value = s;
            suggestionsDiv.style.display = "none";
        });
        suggestionsDiv.appendChild(div);
    }
});

let filterToggleBtn = document.getElementById("filterToggleBtn");
let filterPanel = document.getElementById("filterPanel");
let closeFilterBtn = document.getElementById("closeFilterBtn");
filterToggleBtn.addEventListener("click", () => {
    filterPanel.classList.add("open");
});
closeFilterBtn.addEventListener("click", () => {
    filterPanel.classList.remove("open");
});
