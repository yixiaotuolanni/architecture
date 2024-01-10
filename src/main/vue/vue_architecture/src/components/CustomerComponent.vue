<script setup>
import ItemDetail from './ShowItemDetail.vue'
import ShopIcon from './icons/IconShop.vue'
import ItemIcon from './icons/IconItem.vue'
import ShopItemIcon from './icons/IconShopItem.vue'
import OrderIcon from "@/components/icons/IconOrder.vue";

import {onMounted, ref} from 'vue';
import {ElCard, ElDialog, ElTable, ElTableColumn} from 'element-plus';
import axios from "axios";
import {axiosInstance} from "@/main.js";
import router from "@/router/index.js";

const hoveredStore = ref(null);
const isShowAddShopItemDialog = ref(false)
const shops = ref([])
const orders = ref([])
const token = localStorage.getItem('token');
const selectedShop = ref(null)
const showProductsPanel = ref(false)
const customer = ref(null)
function findAllShops(){
// 检查是否成功获取令牌并使用它
  if (token) {
    // 在这里可以将令牌发送给服务器或者进行其他操作
    console.log('Token:', token);
    axiosInstance.get("/shop/getall")
      .then(response => {
        // 处理登录成功的响应
        for (const shop of response.data){
          shop.shopItems.forEach(i => {
            i.amount = 0;
            // if (customer.value.orders.lineItems.value.items)
          })
        }
        for (const order of orders.value) {
          if (!order.isPayed){
            for (const lineItem of order.lineItems) {
              const amount = lineItem.amount;
              const shopItemId = lineItem.shopItem.id;

              for (const shop of response.data) {
                for (const shopItem of shop.shopItems) {
                  if (shopItem.id === shopItemId) {
                    shopItem.amount = amount;
                    break;
                  }
                }
              }
            }
          }
        }
        shops.value = response.data
        selectedShop.value = shops.value[0]
      }).catch(error => {
      // 处理登录失败的情况
      // 可以在这里提示用户登录失败的原因
      console.error('登录失败:', error);
    });
  } else {
    console.log('Token not found in localStorage');
  }
}
function findCustomer(){
// 检查是否成功获取令牌并使用它
  if (token) {
    // 在这里可以将令牌发送给服务器或者进行其他操作
    console.log('Token:', token);
    axiosInstance.post("/customer/findCustomer",{
      token : token,
    },{
      headers : {
        "Content-Type" : "application/x-www-form-urlencoded"
      }
    }).then(response => {
      customer.value = response.data
      orders.value = customer.value.orders

      findAllShops()
    }).catch(error => {
      // 处理登录失败的情况
      // 可以在这里提示用户登录失败的原因
      console.error('登录失败:', error);
    });
  } else {
    console.log('Token not found in localStorage');
  }
}
onMounted(() => {
  findCustomer()
});

const refItemTable = ref(null);
const highlightStore = (storeId, isHovered) => {
  hoveredStore.value = isHovered ? storeId : null;
};

function showAddShopItemDialog() {
  isShowAddShopItemDialog.value = true;
}

function switchShop(shop) {
  selectedShop.value = shop
}


function handleAmountChange(row) {
  // 发送请求至后端
  axiosInstance.post('/customer/addLineItem', {
    token : token,
    shopItemId: row.item.id,
    amount: row.amount
  },{
    headers : {
      "Content-Type" : "application/x-www-form-urlencoded"
    }
  })
    .then(response => {
      // 请求成功的处理逻辑
      console.log('Amount changed successfully:', response.data);
    })
    .catch(error => {
      // 请求失败的处理逻辑
      console.error('Error changing amount:', error);
    });
}

function formatIsPayed(row, column, cellValue) {
  // 如果 is_payed 为真，则显示为已支付，否则为未支付
  return cellValue ? '已支付' : '未支付';
}

function payOrder(orderId){
  axiosInstance.post("/customer/pay",{
    token : token,
    orderId : orderId,
  },{
    headers : {
      "Content-Type" : "application/x-www-form-urlencoded"
    }
  }).then(response => {
    if (response.status === 200){
      findCustomer()
    }
  })
}

</script>

<template>
  <ItemDetail>
    <template #icon>
      <ShopIcon/>
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
    </div>
  </ItemDetail>

  <ItemDetail>
    <template #icon>
      <ShopItemIcon/>
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
              <el-table-column label="数量">
                <template #default="scope">
                  <el-input-number v-model="scope.row.amount" :min="0" :max="10" size="small" @change="handleAmountChange(scope.row)"></el-input-number>
                </template>
              </el-table-column>
              <!-- 根据需要添加更多列 -->
            </el-table>
          </div>
        </el-collapse-item>
      </el-collapse>
    </div>
  </ItemDetail>

  <ItemDetail>
    <template #icon>
      <OrderIcon/>
    </template>
    <template #heading>order</template>

    <div>
      <el-collapse v-if="customer" v-model="showProductsPanel">
        <el-collapse-item :title="customer.name">
          <div>
            <h2>{{ customer.name }} 的订单列表</h2>
            <el-table :data="orders" style="width: 100%" border>
<!--              <el-table-column type="selection"></el-table-column>-->
              <el-table-column prop="id" label="订单号"></el-table-column>
              <el-table-column prop="isPayed" label="支付状态" :formatter="formatIsPayed"></el-table-column>
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
              <el-table-column prop="shop.name" label="店铺名字"></el-table-column>
              <el-table-column prop="shop.phoneNumber" label="店铺电话"></el-table-column>
              <el-table-column prop="totalPrice" label="总计"></el-table-column>
              <el-table-column label="支付订单">
                <template #default="{ row }">
                  <el-button :disabled="row.isPayed" type="primary" @click="payOrder(row.id)">支付订单</el-button>
                  <!-- 如果订单已支付，不显示支付按钮 -->
                </template>
              </el-table-column>
              <!-- 其他订单信息列 -->
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

</style>
