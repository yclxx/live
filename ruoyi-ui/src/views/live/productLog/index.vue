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
            <el-form-item label="日期" prop="infoDate">
              <el-input v-model="queryParams.infoDate" placeholder="请输入日期" clearable @keyup.enter="handleQuery" />
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
              v-hasPermi="['live:productLog:add']">刷新数据</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="success" plain icon="Edit" :disabled="single" @click="handleUpdate()"
              v-hasPermi="['live:productLog:edit']">修改</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="danger" plain icon="Delete" :disabled="multiple" @click="handleDelete()"
              v-hasPermi="['live:productLog:remove']">删除</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="warning" plain icon="Download" @click="handleExport"
              v-hasPermi="['live:productLog:export']">导出</el-button>
          </el-col>
          <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
        </el-row>
      </template>

      <el-table v-loading="loading" :data="productLogList" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="主键" align="center" prop="id" v-if="false" />
        <el-table-column label="产品代码" align="center" prop="productCode" width="78" />
        <el-table-column label="产品名称" align="center" prop="productName" />
        <el-table-column label="日期" align="center" prop="infoDate" width="98" />
        <el-table-column label="开盘价" align="center" prop="f17" width="68" />
        <el-table-column label="收盘价" align="center" prop="f2" width="68" />
        <el-table-column label="最高价" align="center" prop="f15" width="68" />
        <el-table-column label="最低价" align="center" prop="f16" width="68" />
        <el-table-column label="成交量" align="center" prop="f5" :show-overflow-tooltip="true" width="78">
          <template #default="scope">
            <span>{{ numToUnitNum(scope.row.f5) }}</span>
            <span>{{ getUnit(scope.row.f5,'手') }}</span>
          </template>
        </el-table-column>
        <el-table-column label="成交额" align="center" prop="f6" :show-overflow-tooltip="true">
          <template #default="scope">
            <span>{{ numToUnitNum(scope.row.f6) }}</span>
            <span>{{ getUnit(scope.row.f6) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="振幅(%)" align="center" prop="f7" width="78" />
        <el-table-column label="涨跌幅" align="center" prop="f3" width="68" />
        <el-table-column label="涨跌额" align="center" prop="f4" width="68" />
        <el-table-column label="换手率" align="center" prop="f8" width="68" />
        <el-table-column label="5日均价" align="center" prop="ma5" width="78" />
        <el-table-column label="10日均价" align="center" prop="ma10" width="78" />
        <el-table-column label="20日均价" align="center" prop="ma20" width="78" />
      </el-table>

      <pagination v-show="total > 0" :total="total" v-model:page="queryParams.pageNum"
        v-model:limit="queryParams.pageSize" @pagination="getList" />
    </el-card>
    <!-- 添加或修改产品记录对话框 -->
    <el-dialog :title="dialog.title" v-model="dialog.visible" width="500px" append-to-body>
      <el-form ref="productLogFormRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="产品代码" prop="productCode">
          <el-input v-model="form.productCode" placeholder="请输入产品代码" />
        </el-form-item>
        <el-form-item label="产品名称" prop="productName">
          <el-input v-model="form.productName" placeholder="请输入产品名称" />
        </el-form-item>
        <el-form-item label="日期" prop="infoDate">
          <el-input v-model="form.infoDate" placeholder="请输入日期" />
        </el-form-item>
        <el-form-item label="开盘价" prop="f17">
          <el-input v-model="form.f17" placeholder="请输入开盘价" />
        </el-form-item>
        <el-form-item label="收盘价" prop="f2">
          <el-input v-model="form.f2" placeholder="请输入收盘价" />
        </el-form-item>
        <el-form-item label="最高价" prop="f15">
          <el-input v-model="form.f15" placeholder="请输入最高价" />
        </el-form-item>
        <el-form-item label="最低价" prop="f16">
          <el-input v-model="form.f16" placeholder="请输入最低价" />
        </el-form-item>
        <el-form-item label="成交量" prop="f5">
          <el-input v-model="form.f5" placeholder="请输入成交量" />
        </el-form-item>
        <el-form-item label="成交额" prop="f6">
          <el-input v-model="form.f6" placeholder="请输入成交额" />
        </el-form-item>
        <el-form-item label="振幅(%)" prop="f7">
          <el-input v-model="form.f7" placeholder="请输入振幅(%)" />
        </el-form-item>
        <el-form-item label="涨跌幅(%)" prop="f3">
          <el-input v-model="form.f3" placeholder="请输入涨跌幅(%)" />
        </el-form-item>
        <el-form-item label="涨跌额" prop="f4">
          <el-input v-model="form.f4" placeholder="请输入涨跌额" />
        </el-form-item>
        <el-form-item label="换手率" prop="f8">
          <el-input v-model="form.f8" placeholder="请输入换手率" />
        </el-form-item>
        <el-form-item label="5日均价" prop="ma5">
          <el-input v-model="form.ma5" placeholder="请输入5日均价" />
        </el-form-item>
        <el-form-item label="10日均价" prop="ma10">
          <el-input v-model="form.ma10" placeholder="请输入10日均价" />
        </el-form-item>
        <el-form-item label="20日均价" prop="ma20">
          <el-input v-model="form.ma20" placeholder="请输入20日均价" />
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

<script setup name="ProductLog" lang="ts">
  import { listProductLog, getProductLog, delProductLog, addProductLog, updateProductLog } from '@/api/live/productLog';
  import { ProductLogVO, ProductLogQuery, ProductLogForm } from '@/api/live/productLog/types';

  const { proxy } = getCurrentInstance() as ComponentInternalInstance;

  const productLogList = ref<ProductLogVO[]>([]);
  const buttonLoading = ref(false);
  const loading = ref(true);
  const showSearch = ref(true);
  const ids = ref<Array<string | number>>([]);
  const single = ref(true);
  const multiple = ref(true);
  const total = ref(0);

  const queryFormRef = ref<ElFormInstance>();
  const productLogFormRef = ref<ElFormInstance>();

  const dialog = reactive<DialogOption>({
    visible: false,
    title: ''
  });

  const initFormData : ProductLogForm = {
    id: undefined,
    productCode: undefined,
    productName: undefined,
    infoDate: undefined,
    f17: undefined,
    f2: undefined,
    f15: undefined,
    f16: undefined,
    f5: undefined,
    f6: undefined,
    f7: undefined,
    f3: undefined,
    f4: undefined,
    f8: undefined,
    ma5: undefined,
    ma10: undefined,
    ma20: undefined,
  }
  const data = reactive<PageData<ProductLogForm, ProductLogQuery>>({
    form: { ...initFormData },
    queryParams: {
      pageNum: 1,
      pageSize: 10,
      orderByColumn: 'id',
      isAsc: 'desc',
      productCode: undefined,
      productName: undefined,
      infoDate: undefined,
      params: {
      }
    },
    rules: {
      id: [
        { required: true, message: "主键不能为空", trigger: "blur" }
      ],
      productCode: [
        { required: true, message: "产品代码不能为空", trigger: "blur" }
      ],
      productName: [
        { required: true, message: "产品名称不能为空", trigger: "blur" }
      ],
      infoDate: [
        { required: true, message: "日期不能为空", trigger: "blur" }
      ],
    }
  });

  const { queryParams, form, rules } = toRefs(data);

  /** 查询产品记录列表 */
  const getList = async () => {
    loading.value = true;
    const res = await listProductLog(queryParams.value);
    productLogList.value = res.rows;
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
    productLogFormRef.value?.resetFields();
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
  const handleSelectionChange = (selection : ProductLogVO[]) => {
    ids.value = selection.map(item => item.id);
    single.value = selection.length != 1;
    multiple.value = !selection.length;
  }

  /** 新增按钮操作 */
  const handleAdd = async () => {
    await proxy?.$modal.confirm('是否更新数据？').finally(() => loading.value = false);
    await addProductLog().finally(() => buttonLoading.value = false);
    proxy?.$modal.msgSuccess("操作成功");
    await getList();
  }

  /** 修改按钮操作 */
  const handleUpdate = async (row ?: ProductLogVO) => {
    reset();
    const _id = row?.id || ids.value[0]
    const res = await getProductLog(_id);
    Object.assign(form.value, res.data);
    dialog.visible = true;
    dialog.title = "修改产品记录";
  }

  /** 提交按钮 */
  const submitForm = () => {
    productLogFormRef.value?.validate(async (valid : boolean) => {
      if (valid) {
        buttonLoading.value = true;
        if (form.value.id) {
          await updateProductLog(form.value).finally(() => buttonLoading.value = false);
        }
        proxy?.$modal.msgSuccess("操作成功");
        dialog.visible = false;
        await getList();
      }
    });
  }

  /** 删除按钮操作 */
  const handleDelete = async (row ?: ProductLogVO) => {
    const _ids = row?.id || ids.value;
    await proxy?.$modal.confirm('是否确认删除产品记录编号为"' + _ids + '"的数据项？').finally(() => loading.value = false);
    await delProductLog(_ids);
    proxy?.$modal.msgSuccess("删除成功");
    await getList();
  }

  /** 导出按钮操作 */
  const handleExport = () => {
    proxy?.download('live/productLog/export', {
      ...queryParams.value
    }, `productLog_${new Date().getTime()}.xlsx`)
  }

  onMounted(() => {
    getList();
  });
</script>
