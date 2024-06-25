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
            <el-button type="primary" plain icon="Plus" @click="handleAdd"
              v-hasPermi="['live:productMoneyLog:add']">新增</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="success" plain icon="Edit" :disabled="single" @click="handleUpdate()"
              v-hasPermi="['live:productMoneyLog:edit']">修改</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="danger" plain icon="Delete" :disabled="multiple" @click="handleDelete()"
              v-hasPermi="['live:productMoneyLog:remove']">删除</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="warning" plain icon="Download" @click="handleExport"
              v-hasPermi="['live:productMoneyLog:export']">导出</el-button>
          </el-col>
          <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
        </el-row>
      </template>

      <el-table v-loading="loading" :data="productMoneyLogList" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="主键" align="center" prop="id" v-if="false" />
        <el-table-column label="产品代码" align="center" prop="productCode" width="78" />
        <el-table-column label="产品名称" align="center" prop="productName" width="98" />
        <el-table-column label="日期" align="center" prop="infoDate" width="98" />
        <el-table-column label="主力净流入" align="center">
          <el-table-column label="净额" align="center" prop="f62" width="98" :show-overflow-tooltip="true">
            <template #default="scope">
              <span>{{ numToUnitNum(scope.row.f62) }}</span>
              <span>{{ getUnit(scope.row.f62) }}</span>
            </template>
          </el-table-column>
          <el-table-column label="净占比" align="center" prop="f184" />
        </el-table-column>
        <el-table-column label="超大单流入" align="center">
          <el-table-column label="净额" align="center" prop="f66" width="98" :show-overflow-tooltip="true">
            <template #default="scope">
              <span>{{ numToUnitNum(scope.row.f66) }}</span>
              <span>{{ getUnit(scope.row.f66) }}</span>
            </template>
          </el-table-column>
          <el-table-column label="净占比" align="center" prop="f69" />
        </el-table-column>
        <el-table-column label="大单净流入" align="center">
          <el-table-column label="净额" align="center" prop="f72" width="98" :show-overflow-tooltip="true">
            <template #default="scope">
              <span>{{ numToUnitNum(scope.row.f72) }}</span>
              <span>{{ getUnit(scope.row.f72) }}</span>
            </template>
          </el-table-column>
          <el-table-column label="净占比" align="center" prop="f75" />
        </el-table-column>
        <el-table-column label="中单净流入" align="center">
          <el-table-column label="净额" align="center" prop="f78" width="98" :show-overflow-tooltip="true">
            <template #default="scope">
              <span>{{ numToUnitNum(scope.row.f78) }}</span>
              <span>{{ getUnit(scope.row.f78) }}</span>
            </template>
          </el-table-column>
          <el-table-column label="净占比" align="center" prop="f81" />
        </el-table-column>
        <el-table-column label="小单净流入" align="center">
          <el-table-column label="净额" align="center" prop="f84" width="98" :show-overflow-tooltip="true">
            <template #default="scope">
              <span>{{ numToUnitNum(scope.row.f84) }}</span>
              <span>{{ getUnit(scope.row.f84) }}</span>
            </template>
          </el-table-column>
          <el-table-column label="净占比" align="center" prop="f87" />
        </el-table-column>
        <el-table-column label="更新时间" align="center" prop="updateTime" width="180">
          <template #default="scope">
            <span>{{ parseTime(scope.row.updateTime) }}</span>
          </template>
        </el-table-column>
      </el-table>

      <pagination v-show="total > 0" :total="total" v-model:page="queryParams.pageNum"
        v-model:limit="queryParams.pageSize" @pagination="getList" />
    </el-card>
    <!-- 添加或修改资金流向对话框 -->
    <el-dialog :title="dialog.title" v-model="dialog.visible" width="500px" append-to-body>
      <el-form ref="productMoneyLogFormRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="产品代码" prop="productCode">
          <el-input v-model="form.productCode" placeholder="请输入产品代码" />
        </el-form-item>
        <el-form-item label="产品名称" prop="productName">
          <el-input v-model="form.productName" placeholder="请输入产品名称" />
        </el-form-item>
        <el-form-item label="日期" prop="infoDate">
          <el-input v-model="form.infoDate" placeholder="请输入日期" />
        </el-form-item>
        <el-form-item label="主力净流入" prop="f62">
          <el-input v-model="form.f62" placeholder="请输入主力净流入" />
        </el-form-item>
        <el-form-item label="小单净流入" prop="f84">
          <el-input v-model="form.f84" placeholder="请输入小单净流入" />
        </el-form-item>
        <el-form-item label="中单净流入" prop="f78">
          <el-input v-model="form.f78" placeholder="请输入中单净流入" />
        </el-form-item>
        <el-form-item label="大单净流入" prop="f72">
          <el-input v-model="form.f72" placeholder="请输入大单净流入" />
        </el-form-item>
        <el-form-item label="超大单净流入" prop="f66">
          <el-input v-model="form.f66" placeholder="请输入超大单净流入" />
        </el-form-item>
        <el-form-item label="主力净流入 净占比" prop="f184">
          <el-input v-model="form.f184" placeholder="请输入主力净流入 净占比" />
        </el-form-item>
        <el-form-item label="小单净流入 净占比" prop="f87">
          <el-input v-model="form.f87" placeholder="请输入小单净流入 净占比" />
        </el-form-item>
        <el-form-item label="中单净流入 净占比" prop="f81">
          <el-input v-model="form.f81" placeholder="请输入中单净流入 净占比" />
        </el-form-item>
        <el-form-item label="大单净流入 净占比" prop="f75">
          <el-input v-model="form.f75" placeholder="请输入大单净流入 净占比" />
        </el-form-item>
        <el-form-item label="超大单净流入 净占比" prop="f69">
          <el-input v-model="form.f69" placeholder="请输入超大单净流入 净占比" />
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

<script setup name="ProductMoneyLog" lang="ts">
  import { listProductMoneyLog, getProductMoneyLog, delProductMoneyLog, addProductMoneyLog, updateProductMoneyLog } from '@/api/live/productMoneyLog';
  import { ProductMoneyLogVO, ProductMoneyLogQuery, ProductMoneyLogForm } from '@/api/live/productMoneyLog/types';

  const { proxy } = getCurrentInstance() as ComponentInternalInstance;

  const productMoneyLogList = ref<ProductMoneyLogVO[]>([]);
  const buttonLoading = ref(false);
  const loading = ref(true);
  const showSearch = ref(true);
  const ids = ref<Array<string | number>>([]);
  const single = ref(true);
  const multiple = ref(true);
  const total = ref(0);

  const queryFormRef = ref<ElFormInstance>();
  const productMoneyLogFormRef = ref<ElFormInstance>();

  const dialog = reactive<DialogOption>({
    visible: false,
    title: ''
  });

  const initFormData : ProductMoneyLogForm = {
    id: undefined,
    productCode: undefined,
    productName: undefined,
    infoDate: undefined,
    f62: undefined,
    f84: undefined,
    f78: undefined,
    f72: undefined,
    f66: undefined,
    f184: undefined,
    f87: undefined,
    f81: undefined,
    f75: undefined,
    f69: undefined,
  }
  const data = reactive<PageData<ProductMoneyLogForm, ProductMoneyLogQuery>>({
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
    }
  });

  const { queryParams, form, rules } = toRefs(data);

  /** 查询资金流向列表 */
  const getList = async () => {
    loading.value = true;
    const res = await listProductMoneyLog(queryParams.value);
    productMoneyLogList.value = res.rows;
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
    productMoneyLogFormRef.value?.resetFields();
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
  const handleSelectionChange = (selection : ProductMoneyLogVO[]) => {
    ids.value = selection.map(item => item.id);
    single.value = selection.length != 1;
    multiple.value = !selection.length;
  }

  /** 新增按钮操作 */
  const handleAdd = () => {
    reset();
    dialog.visible = true;
    dialog.title = "添加资金流向";
  }

  /** 修改按钮操作 */
  const handleUpdate = async (row ?: ProductMoneyLogVO) => {
    reset();
    const _id = row?.id || ids.value[0]
    const res = await getProductMoneyLog(_id);
    Object.assign(form.value, res.data);
    dialog.visible = true;
    dialog.title = "修改资金流向";
  }

  /** 提交按钮 */
  const submitForm = () => {
    productMoneyLogFormRef.value?.validate(async (valid : boolean) => {
      if (valid) {
        buttonLoading.value = true;
        if (form.value.id) {
          await updateProductMoneyLog(form.value).finally(() => buttonLoading.value = false);
        } else {
          await addProductMoneyLog(form.value).finally(() => buttonLoading.value = false);
        }
        proxy?.$modal.msgSuccess("操作成功");
        dialog.visible = false;
        await getList();
      }
    });
  }

  /** 删除按钮操作 */
  const handleDelete = async (row ?: ProductMoneyLogVO) => {
    const _ids = row?.id || ids.value;
    await proxy?.$modal.confirm('是否确认删除资金流向编号为"' + _ids + '"的数据项？').finally(() => loading.value = false);
    await delProductMoneyLog(_ids);
    proxy?.$modal.msgSuccess("删除成功");
    await getList();
  }

  /** 导出按钮操作 */
  const handleExport = () => {
    proxy?.download('live/productMoneyLog/export', {
      ...queryParams.value
    }, `productMoneyLog_${new Date().getTime()}.xlsx`)
  }

  onMounted(() => {
    getList();
  });
</script>
