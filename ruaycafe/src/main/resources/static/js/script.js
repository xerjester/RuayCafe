    // Cart state
    let cart = [];
    let currentProduct = null;
    let modalState = {
        size: 'medium',
        sweetLevel: 100,
        iceLevel: 100,
        addons: [],
        quantity: 1,
        note: ''
    };

    // Open customize modal
    function openCustomizeModal(element) {
        const productId = element.getAttribute('data-id');
        const productName = element.querySelector('.menu-item-name').textContent;
        const productPrice = parseFloat(element.querySelector('.menu-item-price').textContent.replace('฿', ''));

        currentProduct = {
            id: productId,
            name: productName,
            basePrice: productPrice
        };

        // Reset modal state
        modalState = {
            size: 'medium',
            sweetLevel: 100,
            iceLevel: 100,
            addons: [],
            quantity: 1,
            note: ''
        };

        // Update modal
        document.getElementById('modalProductName').textContent = productName;
        document.getElementById('sweetLevel').value = 100;
        document.getElementById('iceLevel').value = 100;
        document.getElementById('sweetValue').textContent = '100%';
        document.getElementById('iceValue').textContent = '100%';
        document.getElementById('modalQuantity').textContent = '1';
        document.getElementById('specialNote').value = '';

        // Reset size selection
        document.querySelectorAll('.size-option').forEach(opt => opt.classList.remove('active'));
        document.querySelector('[data-size="medium"]').classList.add('active');

        // Reset addons
        document.querySelectorAll('.addon-item').forEach(item => {
            item.classList.remove('selected');
            item.querySelector('.addon-checkbox').checked = false;
        });

        updateModalPrice();
        document.getElementById('customizeModal').style.display = 'block';
    }

    function closeModal() {
        document.getElementById('customizeModal').style.display = 'none';
    }

    function selectSize(size) {
        modalState.size = size;
        document.querySelectorAll('.size-option').forEach(opt => opt.classList.remove('active'));
        document.querySelector(`[data-size="${size}"]`).classList.add('active');
        updateModalPrice();
    }

    function updateSliderValue(elementId, value) {
        document.getElementById(elementId).textContent = value + '%';
        if (elementId === 'sweetValue') modalState.sweetLevel = parseInt(value);
        if (elementId === 'iceValue') modalState.iceLevel = parseInt(value);
    }

    function toggleAddon(element, addonId) {
        const checkbox = element.querySelector('.addon-checkbox');
        checkbox.checked = !checkbox.checked;
        element.classList.toggle('selected');

        const addonPrice = parseFloat(element.querySelector('.addon-price').textContent.replace('+฿', ''));
        const addonName = element.querySelector('.addon-name').textContent;

        if (checkbox.checked) {
            modalState.addons.push({ id: addonId, name: addonName, price: addonPrice });
        } else {
            modalState.addons = modalState.addons.filter(a => a.id !== addonId);
        }

        updateModalPrice();
    }

    function changeQuantity(change) {
        modalState.quantity = Math.max(1, modalState.quantity + change);
        document.getElementById('modalQuantity').textContent = modalState.quantity;
        updateModalPrice();
    }

    function updateModalPrice() {
        let price = currentProduct.basePrice;

        // Add size price
        if (modalState.size === 'medium') price += 10;
        if (modalState.size === 'large') price += 20;

        // Add addon prices
        modalState.addons.forEach(addon => price += addon.price);

        const total = price * modalState.quantity;
        document.getElementById('modalTotalPrice').textContent = '฿' + total;
    }

    function addToCart() {
        modalState.note = document.getElementById('specialNote').value;

        const item = {
            productId: currentProduct.id,
            productName: currentProduct.name,
            basePrice: currentProduct.basePrice,
            ...modalState,
            totalPrice: calculateItemPrice()
        };

        cart.push(item);
        renderCart();
        closeModal();
    }

    function calculateItemPrice() {
        let price = currentProduct.basePrice;
        if (modalState.size === 'medium') price += 10;
        if (modalState.size === 'large') price += 20;
        modalState.addons.forEach(addon => price += addon.price);
        return price * modalState.quantity;
    }

    function renderCart() {
        const container = document.getElementById('orderItems');

        if (cart.length === 0) {
            container.innerHTML = `
                <div class="empty-cart">
                    <div style="font-size: 60px; margin-bottom: 15px;">🛒</div>
                    <div>ยังไม่มีรายการสั่งซื้อ</div>
                    <div style="font-size: 12px; color: #bbb; margin-top: 8px;">คลิกเมนูเพื่อเพิ่มรายการ</div>
                </div>
            `;
        } else {
            container.innerHTML = cart.map((item, index) => `
                <div class="order-item">
                    <div class="order-item-header">
                        <div>
                            <div class="order-item-name">${item.productName}</div>
                            <div class="customization-tags">
                                <span class="tag">ขนาด: ${item.size === 'small' ? 'S' : item.size === 'medium' ? 'M' : 'L'}</span>
                                <span class="tag">หวาน ${item.sweetLevel}%</span>
                                <span class="tag">น้ำแข็ง ${item.iceLevel}%</span>
                                ${item.addons.map(a => `<span class="tag addon">+${a.name}</span>`).join('')}
                            </div>
                            ${item.note ? `<div style="font-size: 12px; color: #999; margin-top: 5px;">📝 ${item.note}</div>` : ''}
                        </div>
                        <div class="order-item-price">฿${item.totalPrice}</div>
                    </div>
                    <div class="quantity-controls">
                        <div style="display: flex; align-items: center; gap: 10px;">
                            <button class="qty-btn" onclick="removeFromCart(${index})">🗑️</button>
                            <span>จำนวน: ${item.quantity}</span>
                        </div>
                    </div>
                </div>
            `).join('');
        }

        updateSummary();
    }

    function removeFromCart(index) {
        cart.splice(index, 1);
        renderCart();
    }

    function updateSummary() {
        const subtotal = cart.reduce((sum, item) => sum + item.totalPrice, 0);
        const itemCount = cart.reduce((sum, item) => sum + item.quantity, 0);
        const estimatedCost = subtotal * 0.35; // สมมติต้นทุน 35%
        const profit = subtotal - estimatedCost;

        document.getElementById('itemCount').textContent = itemCount;
        document.getElementById('subtotal').textContent = '฿' + subtotal.toFixed(2);
        document.getElementById('discount').textContent = '฿0';
        document.getElementById('totalCost').textContent = '฿' + estimatedCost.toFixed(2);
        document.getElementById('totalProfit').textContent = '฿' + profit.toFixed(2);
        document.getElementById('total').textContent = '฿' + subtotal.toFixed(2);
    }

    async function checkout() {
        if (cart.length === 0) {
            alert('กรุณาเลือกรายการสั่งซื้อ');
            return;
        }

        // Send to backend
        try {
            const response = await fetch('/api/orders', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ items: cart })
            });

            const data = await response.json();
            if (data.ok) {
                alert(`ชำระเงินสำเร็จ!\n\nยอดรวม: ฿${data.total}\nกำไร: ฿${data.profit}\nเลขที่: ${data.orderNumber}`);
                cart = [];
                renderCart();

                // Update order number
                const orderNum = parseInt(document.getElementById('orderNumber').textContent) + 1;
                document.getElementById('orderNumber').textContent = String(orderNum).padStart(3, '0');
            } else {
                alert('เกิดข้อผิดพลาด: ' + (data.msg || 'ไม่สามารถบันทึกได้'));
            }
        } catch (error) {
            console.error('Error:', error);
            alert('ไม่สามารถเชื่อมต่อเซิร์ฟเวอร์');
        }
    }

    // Close modal on outside click
    window.onclick = function(event) {
        const modal = document.getElementById('customizeModal');
        if (event.target == modal) {
            closeModal();
        }
    };

    function openAddMenuModal() {
        document.getElementById("addMenuModal").style.display = "block";
    }

    function closeAddMenuModal() {
        document.getElementById("addMenuModal").style.display = "none";
    }


    function submitNewMenu() {
        const name = document.getElementById('menuName').value;
        const price = parseFloat(document.getElementById('menuPrice').value);
        const category = document.getElementById('menuCategory').value;
        // image handling ตามต้องการ
        const ingredients = [];
        document.querySelectorAll('#ingredientList .ingredient-item').forEach(item => {
            const checkbox = item.querySelector('.ingredient-checkbox');
            const qty = parseFloat(item.querySelector('.ingredient-qty').value) || 0;
            if (checkbox.checked && qty > 0) {
                ingredients.push({ ingredientId: checkbox.dataset.id, quantity: qty });
            }
        });


        // ส่งไป backend
        fetch('/api/products', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ name, price, category, ingredients })
        })
        .then(res => res.json())
        .then(data => {
            if(data.ok){
                alert('เพิ่มเมนูสำเร็จ');
                closeAddMenuModal();
                location.reload(); // หรืออัปเดตรายการเมนูแบบ AJAX
            } else {
                alert(data.msg);
            }
        });
    }


