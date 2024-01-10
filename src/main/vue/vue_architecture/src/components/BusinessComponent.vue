<script setup>
import ItemDetail from './ShowItemDetail.vue'
import ShopIcon from './icons/IconShop.vue'
import ItemIcon from './icons/IconItem.vue'
import ShopItemIcon from './icons/IconShopItem.vue'
import OrderIcon from "@/components/icons/IconOrder.vue";

import {computed, onMounted, ref} from 'vue';
import { ElCard, ElDialog, ElTable, ElTableColumn } from 'element-plus';
import axios from "axios";
import {axiosInstance} from "@/main.js";
import router from "@/router/index.js";

const token = localStorage.getItem('token');

const hoveredStore = ref(null);
const isShowAddShopDialog = ref(false)
const isShowAddItemDialog = ref(false)
const isShowAddShopItemDialog = ref(false)
const showProductsPanel = ref(false)
const orders = ref(null)

const business = ref(null)
const shops = ref([]);
const selectedShop = ref(null)
const items = ref([]);
const selectedItemsIds = ref([])
const newShop = ref({
  name : '',
  phoneNumber : "",
  address : "",
  description : ""
})

const newItem = ref({
  name : '',
  price : 0.00,
  description : ""
})
function handleSelectionChange(selection) {
  // 获取选中的行的 ID
  selectedItemsIds.value = selection.map(item => item.id);
}
const itemsNotInSelectedShop = ref([])
function getItemsNotInSelectedShop(){
  itemsNotInSelectedShop.value = items.value.filter(i => !selectedShop.value.shopItems.some(shopItem => (shopItem.item.id) === (i.id)));
}

onMounted(() => {
  findBusiness();
});

function findBusiness(){
// 检查是否成功获取令牌并使用它
  if (token) {
    // 在这里可以将令牌发送给服务器或者进行其他操作
    console.log('Token:', token);
    axiosInstance.post("/business/findBusiness",{
      token : token,
    },{
      headers : {
        "Content-Type" : "application/x-www-form-urlencoded"
      }
    }).then(response => {
      // 处理登录成功的响应
      business.value = response.data
      shops.value.push(...business.value.shops)
      items.value.push(...business.value.items)
      selectedShop.value = shops.value[0]
      getItemsNotInSelectedShop()
      findShopOrders()
    }).catch(error => {
      // 处理登录失败的情况
      // 可以在这里提示用户登录失败的原因
      console.error('登录失败:', error);
    });
  } else {
    console.log('Token not found in localStorage');
  }
}
const highlightStore = (storeId, isHovered) => {
  hoveredStore.value = isHovered ? storeId : null;
};

function showAddShopDialog(){
  isShowAddShopDialog.value = true;
}

function showAddItemDialog(){
  isShowAddItemDialog.value = true;
}
function showAddShopItemDialog(){
  isShowAddShopItemDialog.value = true;
}
function switchShop(shop){
  selectedShop.value = shop
  findShopOrders()
}

function findShopOrders(){
  axiosInstance.post("business/findShopOrders",{
    token : token,
    shopId : selectedShop.value.id,
  },{
    headers : {
      "Content-Type" : "application/x-www-form-urlencoded"
    }
  }).then(response => {
    if (response.status === 200){
      orders.value = response.data
    }
  }).catch(error => {
    console.error('查询账单失败', error);
  });
}

const addShop = async () => {
  try {
    // 发送添加商店的 POST 请求示例
    const response = await axiosInstance.post('/business/addShop', {
      token : token,
      shopName : newShop.value.name,
      phoneNumber : newShop.value.phoneNumber,
      address : newShop.value.address,
      shopDescription : newShop.value.description
    },{headers : {
      "Content-Type" : "application/x-www-form-urlencoded"
      }}).then((response) =>{
        if(response.status === 200){
          shops.value = []
          items.value = []
          newShop.value.name = ""
          newShop.value.phoneNumber = ""
          newShop.value.address = ""
          newShop.value.description = ""
          findBusiness()
          console.log('成功添加商店', response.data);
        }
    }).catch(error => {
      console.error('添加商店失败', error);
    });
    isShowAddShopDialog.value = false
    // 处理成功响应，更新界面或其他操作
  } catch (error) {
    // 处理错误
    console.error('添加商店失败', error);
  }
};

const addItem = async () => {
  try {
    // 发送添加商店的 POST 请求示例
    await axiosInstance.post('/business/addItem', {
      token : token,
      itemName : newItem.value.name,
      itemPrice : newItem.value.price,
      itemDescription : newItem.value.description
    },{headers : {
        "Content-Type" : "application/x-www-form-urlencoded"
      }}).then((response) =>{
      if(response.status === 200){
        shops.value = []
        items.value = []
        newItem.value.name = ""
        newItem.value.price = 0
        newItem.value.description = ""
        findBusiness()
        console.log('成功添加商店', response.data);
      }
    }).catch(error => {
      console.error('添加商店失败', error);
    });
    isShowAddItemDialog.value = false
    // 处理成功响应，更新界面或其他操作
  } catch (error) {
    // 处理错误
    console.error('添加商品失败', error);
  }
};

const addShopItems = async () => {
  try {
    // 发送添加商店商品关联的 POST 请求示例
    const response = await axiosInstance.post('business/addShopItems', {
      token : token,
      itemsId : selectedItemsIds.value,
      shopId : selectedShop.value.id
    });
    if (response.status === 200){
      shops.value = []
      items.value = []
      findBusiness()
      console.log('成功添加商店商品关联', response.data);
    }
    // 处理成功响应，更新界面或其他操作
    isShowAddShopItemDialog.value = false
  } catch (error) {
    // 处理错误
    console.error('添加商店商品关联失败', error);
  }
};

const clearAddShopForm = () => {
  newShop.value.name = '';
  newShop.value.address = '';
  newShop.value.phone = '';
};

const cancelAddShop = () => {
  clearAddShopForm();
  isShowAddShopDialog.value = false;
};

const clearAddItemForm = () => {
  newItem.value.name = '';
  newItem.value.price = '';
  newItem.value.description = '';
};

const cancelAddItem = () => {
  clearAddItemForm();
  isShowAddItemDialog.value = false;
};

const cancelAddShopItems = () => {
  isShowAddShopItemDialog.value = false;
};

function formatIsPayed(row, column, cellValue) {
  // 如果 is_payed 为真，则显示为已支付，否则为未支付
  return cellValue ? '已支付' : '未支付';
}
function formatIsConfirmed(row, column, cellValue) {
  // 如果 is_payed 为真，则显示为已支付，否则为未支付
  return cellValue ? '已确认' : '未确认';
}
function formatIsRefunded(row, column, cellValue) {
  // 如果 is_payed 为真，则显示为已支付，否则为未支付
  return cellValue ? '已退款' : '未退款';
}
function confirmed(orderId){
  axiosInstance.post("/business/confirm",{
    token : token,
    shopId : selectedShop.value.id,
    orderId : orderId,
  },{
    headers : {
      "Content-Type" : "application/x-www-form-urlencoded"
    }
  }).then(response => {
    if (response.status === 200){
      findBusiness()
    }
  })
}
function refunded(orderId){
  axiosInstance.post("/customer/pay",{
    token : token,
    shopId : selectedShop.value.id,
    orderId : orderId,
  },{
    headers : {
      "Content-Type" : "application/x-www-form-urlencoded"
    }
  }).then(response => {
    if (response.status === 200){
      findBusiness()
    }
  })
}
</script>

<template>
  <ItemDetail>
    <template #icon>
      <ShopIcon />
    </template>
    <template #heading>Shop</template>

    <div>
      <h1>商家店铺列表</h1>
      <el-card
        v-for="shop in shops"
        :key="shop.id"
        class="store-card"
        @click="switchShop(shop)"
        @mouseover="highlightStore(shop.id, true)"
        @mouseleave="highlightStore(shop.id, false)"
        :class="{ 'active-store': shop.id === selectedShop.id, 'hovered-store': shop.id === hoveredStore }"
      >
        <h2>{{ shop.name }}</h2>
        <p>地址：{{ shop.address }}</p>
        <p>电话：{{ shop.phoneNumber }}</p>
        <p>描述：{{ shop.description }}</p>
      </el-card>

      <el-button @click="showAddShopDialog">添加商店</el-button>
      <el-dialog v-model="isShowAddShopDialog" title="添加商店" width="30%">
        <el-form ref="addShopForm" :model="newShop" label-width="80px">
          <el-form-item label="商店名称" required>
            <el-input v-model="newShop.name"></el-input>
          </el-form-item>
          <el-form-item label="地址" required>
            <el-input v-model="newShop.address"></el-input>
          </el-form-item>
          <el-form-item label="电话" required>
            <el-input v-model="newShop.phoneNumber"></el-input>
          </el-form-item>
          <el-form-item label="描述" required>
            <el-input v-model="newShop.description"></el-input>
          </el-form-item>
          <el-form-item class="btn-container">
            <el-button type="primary" @click="addShop">确认添加</el-button>
            <el-button @click="cancelAddShop">取消</el-button>
          </el-form-item>
        </el-form>
      </el-dialog>
    </div>
  </ItemDetail>

  <ItemDetail>
    <template #icon>
      <ItemIcon />
    </template>
    <template #heading>Item</template>

    <div>
      <h1>所有货物列表</h1>
      <el-table :data="items" style="width: 100%">
        <el-table-column prop="name" label="商品名称"></el-table-column>
        <el-table-column prop="price" label="价格"></el-table-column>
        <el-table-column prop="description" label="描述"></el-table-column>
        <!-- 可根据需要添加更多列 -->
      </el-table>

      <el-button @click="showAddItemDialog">添加货物</el-button>
      <el-dialog v-model="isShowAddItemDialog" title="添加货物" width="30%">
        <el-form ref="addProductForm" :model="newItem" label-width="80px">
          <el-form-item label="货物名称" required>
            <el-input v-model="newItem.name"></el-input>
          </el-form-item>
          <el-form-item label="价格" required>
            <el-input v-model="newItem.price"></el-input>
          </el-form-item>
          <el-form-item label="描述" required>
            <el-input v-model="newItem.description"></el-input>
          </el-form-item>
          <el-form-item class="btn-container">
            <el-button type="primary" @click="addItem">确认添加</el-button>
            <el-button @click="cancelAddItem">取消</el-button>
          </el-form-item>
        </el-form>
      </el-dialog>
    </div>
  </ItemDetail>

  <ItemDetail>
    <template #icon>
      <ShopItemIcon />
    </template>
    <template #heading>ShopItem</template>

    <div>
      <el-collapse v-if="selectedShop" v-model="showProductsPanel">
        <el-collapse-item :title="selectedShop.name">
          <div>
            <h2>{{ selectedShop.name }} 的商品列表</h2>
            <el-table :data="selectedShop.shopItems" style="width: 100%">
              <el-table-column prop="item.name" label="商品名称"></el-table-column>
              <el-table-column prop="item.price" label="价格"></el-table-column>
              <el-table-column prop="item.description" label="描述"></el-table-column>
              <!-- 根据需要添加更多列 -->
            </el-table>
          </div>
        </el-collapse-item>
      </el-collapse>

      <el-button @click="showAddShopItemDialog">添加商店货物</el-button>
      <el-dialog v-model="isShowAddShopItemDialog" title="添加商店货物" width="80%">
        <el-table :data="itemsNotInSelectedShop" style="width: 100%" ref="itemTable" border @selection-change="handleSelectionChange">
          <el-table-column type="selection"></el-table-column>
          <el-table-column prop="name" label="商品名称"></el-table-column>
          <el-table-column prop="price" label="价格"></el-table-column>
          <el-table-column prop="description" label="描述"></el-table-column>
          <!-- 其他商品信息列 -->
        </el-table>
        <el-form-item class="btn-container">
          <el-button type="primary" @click="addShopItems">确认添加</el-button>
          <el-button @click="cancelAddShopItems">取消</el-button>
        </el-form-item>
      </el-dialog>
    </div>
  </ItemDetail>

  <ItemDetail>
    <template #icon>
      <OrderIcon/>
    </template>
    <template #heading>order</template>

    <div>
      <el-collapse v-if="orders" v-model="showProductsPanel">
        <el-collapse-item :title="selectedShop.name">
          <div>
            <h2>{{ selectedShop.name }} 的订单列表</h2>
            <el-table :data="orders" style="width: 100%" border>
              <el-table-column prop="id" label="订单号"></el-table-column>
              <el-table-column prop="isPayed" label="支付状态" :formatter="formatIsPayed"></el-table-column>
              <el-table-column prop="isConfirmed" label="确认状态" :formatter="formatIsConfirmed"></el-table-column>
              <el-table-column prop="isRefunded" label="退款状态" :formatter="formatIsRefunded"></el-table-column>
              <el-table-column label="小计" width="300px">
                <template #default="{ row }">
                  <el-table :data="row.lineItems" style="width: 100%">
                    <el-table-column prop="shopItem.item.name" label="名称"></el-table-column>
                    <el-table-column prop="shopItem.item.price" label="价格"></el-table-column>
                    <el-table-column prop="amount" label="数量"></el-table-column>
                    <!-- 其他 lineItem 信息列 -->
                  </el-table>
                </template>
              </el-table-column>
              <el-table-column prop="totalPrice" label="总计"></el-table-column>
              <el-table-column label="确认或取消订单" width="124">
                <template #default="{ row }">
                  <div class="button-container-order">
                    <el-button :disabled="row.isConfirmed || row.isRefunded" type="primary" @click="confirmed(row.id)">确认订单</el-button>
                    <el-button :disabled="row.isConfirmed || row.isRefunded" @click="refunded(row.id)">取消订单</el-button>
                  </div>
                </template>
              </el-table-column>
            </el-table>
          </div>
        </el-collapse-item>
      </el-collapse>
    </div>
  </ItemDetail>


</template>

<style>
.store-card {
  margin-bottom: 20px;
  transition: background-color 0.3s;
}

.hovered-store {
  background-color: #f0f0f0; /* 悬停时的背景颜色 */
}

.active-store {
  background-color: #e6f7ff; /* 选中时的背景颜色 */
}

.btn-container {
  width: 100%;
  display: flex;
  flex-direction: row;
  gap: 10px;
  margin-top: 10px;
  justify-content: space-between;
}

.button-container-order{
  display: flex;
  flex-direction: column;
}
.button-container > * {
  margin-bottom: 8px; /* 设置按钮之间的间距 */
}
</style>
