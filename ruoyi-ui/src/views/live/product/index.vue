<template>
  <div class="p-2">
    <transition :enter-active-class="proxy?.animate.searchAnimate.enter"
      :leave-active-class="proxy?.animate.searchAnimate.leave">
      <div v-show="showSearch" class="mb-[10px]">
        <el-card shadow="hover">
          <el-form ref="queryFormRef" :model="queryParams" :inline="true">
            <el-form-item label="产品代码" prop="productCode">
              <el-input v-model="queryParams.productCode" placeholder="请输入产品代码" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="产品名称" prop="productName">
              <el-input v-model="queryParams.productName" placeholder="请输入产品名称" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="类型" prop="productType">
              <el-select v-model="queryParams.productType" multiple placeholder="请选择类型" clearable>
                <el-option v-for="dict in live_product_type" :key="dict.value" :label="dict.label"
                  :value="dict.value" />
              </el-select>
            </el-form-item>
            <el-form-item label="状态" prop="status">
              <el-select v-model="queryParams.status" placeholder="请选择状态" clearable>
                <el-option v-for="dict in sys_normal_disable" :key="dict.value" :label="dict.label"
                  :value="dict.value" />
              </el-select>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
              <el-button icon="Refresh" @click="resetQuery">重置</el-button>
            </el-form-item>
          </el-form>
        </el-card>
      </div>
    </transition>

    <el-card shadow="never">
      <template #header>
        <el-row :gutter="10" class="mb8">
          <el-col :span="1.5">
            <el-button type="primary" plain icon="Refresh" @click="handleAdd"
              v-hasPermi="['live:product:add']">更新数据</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="success" plain icon="Edit" :disabled="single" @click="handleUpdate()"
              v-hasPermi="['live:product:edit']">修改</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="danger" plain icon="Delete" :disabled="multiple" @click="handleDelete()"
              v-hasPermi="['live:product:remove']">删除</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="warning" plain icon="Download" @click="handleExport"
              v-hasPermi="['live:product:export']">导出</el-button>
          </el-col>
          <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
        </el-row>
      </template>

      <el-table v-loading="loading" :data="productList" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="产品代码" align="center" prop="productCode" v-if="true" />
        <el-table-column label="产品名称" align="center" prop="productName" />
        <el-table-column label="状态" align="center" prop="status">
          <template #default="scope">
            <dict-tag :options="sys_normal_disable" :value="scope.row.status" />
          </template>
        </el-table-column>
        <el-table-column label="类型" align="center" prop="productType">
          <template #default="scope">
            <dict-tag :options="live_product_type" :value="scope.row.productType" />
          </template>
        </el-table-column>
        <el-table-column label="最新价" align="center" prop="gpInfoVo.f2" width="68" />
        <el-table-column label="涨跌幅" align="center" prop="gpInfoVo.f3" width="68" />
        <el-table-column label="最高价" align="center" prop="gpInfoVo.f15" width="68" />
        <el-table-column label="最低价" align="center" prop="gpInfoVo.f16" width="68" />
        <el-table-column label="换手率" align="center" prop="gpInfoVo.f8" width="68" />
        <el-table-column label="5日均价" align="center" prop="gpInfoVo.ma5" width="78" />
        <el-table-column label="10日均价" align="center" prop="gpInfoVo.ma10" width="78" />
        <el-table-column label="20日均价" align="center" prop="gpInfoVo.ma20" width="78" />
        <el-table-column label="总市值" align="center" prop="f116" width="98" :show-overflow-tooltip="true">
          <template #default="scope">
            <span>{{ numToUnitNum(scope.row.f116) }}</span>
            <span>{{ getUnit(scope.row.f116) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="流通市值" align="center" prop="f117" width="98" :show-overflow-tooltip="true">
          <template #default="scope">
            <span>{{ numToUnitNum(scope.row.f117) }}</span>
            <span>{{ getUnit(scope.row.f117) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
          <template #default="scope">
            <el-tooltip content="置顶" placement="top">
              <el-button link type="primary" icon="Edit" @click="handleUpdateTop(scope.row)"
                v-hasPermi="['live:product:edit']"></el-button>
            </el-tooltip>
            <el-tooltip content="删除" placement="top">
              <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)"
                v-hasPermi="['live:product:remove']"></el-button>
            </el-tooltip>
          </template>
        </el-table-column>
      </el-table>

      <pagination v-show="total > 0" :total="total" v-model:page="queryParams.pageNum"
        v-model:limit="queryParams.pageSize" @pagination="getList" />
    </el-card>
    <!-- 添加或修改产品管理对话框 -->
    <el-dialog :title="dialog.title" v-model="dialog.visible" width="500px" append-to-body>
      <el-form ref="productFormRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="产品名称" prop="productName">
          <el-input v-model="form.productName" placeholder="请输入产品名称" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio v-for="dict in sys_normal_disable" :key="dict.value" :value="dict.value">{{dict.label}}</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button :loading="buttonLoading" type="primary" @click="submitForm">确 定</el-button>
          <el-button @click="cancel">取 消</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup name="Product" lang="ts">
  import { listProduct, getProduct, delProduct, addProduct, updateProduct, updateProductTop } from '@/api/live/product';
  import { ProductVO, ProductQuery, ProductForm } from '@/api/live/product/types';

  const { proxy } = getCurrentInstance() as ComponentInternalInstance;
  const { sys_normal_disable } = toRefs<any>(proxy?.useDict('sys_normal_disable'));
  const { live_product_type } = toRefs<any>(proxy?.useDict('live_product_type'));

  const productList = ref<ProductVO[]>([]);
  const buttonLoading = ref(false);
  const loading = ref(true);
  const showSearch = ref(true);
  const ids = ref<Array<string | number>>([]);
  const single = ref(true);
  const multiple = ref(true);
  const total = ref(0);

  const queryFormRef = ref<ElFormInstance>();
  const productFormRef = ref<ElFormInstance>();

  const dialog = reactive<DialogOption>({
    visible: false,
    title: ''
  });

  const initFormData : ProductForm = {
    productCode: undefined,
    productName: undefined,
    productType: undefined,
    status: undefined,
  }
  const data = reactive<PageData<ProductForm, ProductQuery>>({
    form: { ...initFormData },
    queryParams: {
      pageNum: 1,
      pageSize: 10,
      orderByColumn: 'sort,status,product_code',
      isAsc: 'desc,asc,asc',
      productCode: undefined,
      productName: undefined,
      productType: undefined,
      status: undefined,
      params: {
      }
    },
    rules: {
      productCode: [
        { required: true, message: "产品代码不能为空", trigger: "blur" }
      ],
      productName: [
        { required: true, message: "产品名称不能为空", trigger: "blur" }
      ],
      status: [
        { required: true, message: "状态不能为空", trigger: "change" }
      ],
    }
  });

  const { queryParams, form, rules } = toRefs(data);

  /** 查询产品管理列表 */
  const getList = async () => {
    loading.value = true;
    const params = {
      ...queryParams.value,
      productType: queryParams.value.productType.join(',')
    }
    const res = await listProduct(params);
    productList.value = res.rows;
    total.value = res.total;
    loading.value = false;
  }

  /** 取消按钮 */
  const cancel = () => {
    reset();
    dialog.visible = false;
  }

  /** 表单重置 */
  const reset = () => {
    form.value = { ...initFormData };
    productFormRef.value?.resetFields();
  }

  /** 搜索按钮操作 */
  const handleQuery = () => {
    queryParams.value.pageNum = 1;
    getList();
  }

  /** 重置按钮操作 */
  const resetQuery = () => {
    queryFormRef.value?.resetFields();
    handleQuery();
  }

  /** 多选框选中数据 */
  const handleSelectionChange = (selection : ProductVO[]) => {
    ids.value = selection.map(item => item.productCode);
    single.value = selection.length != 1;
    multiple.value = !selection.length;
  }

  /** 新增按钮操作 */
  const handleAdd = async () => {
    await proxy?.$modal.confirm('是否更新数据？').finally(() => loading.value = false);
    await addProduct().finally(() => buttonLoading.value = false);
    proxy?.$modal.msgSuccess("操作成功");
    await getList();
  }

  /** 修改按钮操作 */
  const handleUpdate = async (row ?: ProductVO) => {
    reset();
    const _productCode = row?.productCode || ids.value[0]
    const res = await getProduct(_productCode);
    Object.assign(form.value, res.data);
    dialog.visible = true;
    dialog.title = "修改产品管理";
  }

  /** 提交按钮 */
  const submitForm = () => {
    productFormRef.value?.validate(async (valid : boolean) => {
      if (valid) {
        buttonLoading.value = true;
        if (form.value.productCode) {
          await updateProduct(form.value).finally(() => buttonLoading.value = false);
        } else {
          proxy?.$modal.msgSuccess("操作失败");
        }
        proxy?.$modal.msgSuccess("操作成功");
        dialog.visible = false;
        await getList();
      }
    });
  }

  /** 置顶按钮操作 */
  const handleUpdateTop = async (row ?: ProductVO) => {
    const _productCodes = row?.productCode;
    await proxy?.$modal.confirm('是否确认置顶"' + _productCodes + '"？').finally(() => loading.value = false);
    await updateProductTop(_productCodes);
    proxy?.$modal.msgSuccess("操作成功");
    await getList();
  }
  /** 删除按钮操作 */
  const handleDelete = async (row ?: ProductVO) => {
    const _productCodes = row?.productCode || ids.value;
    await proxy?.$modal.confirm('是否确认删除产品管理编号为"' + _productCodes + '"的数据项？').finally(() => loading.value = false);
    await delProduct(_productCodes);
    proxy?.$modal.msgSuccess("删除成功");
    await getList();
  }

  /** 导出按钮操作 */
  const handleExport = () => {
    proxy?.download('live/product/export', {
      ...queryParams.value
    }, `product_${new Date().getTime()}.xlsx`)
  }

  onMounted(() => {
    getList();
  });
</script>
