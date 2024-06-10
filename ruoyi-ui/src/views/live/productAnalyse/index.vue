<template>
  <div class="p-2">
    <transition :enter-active-class="proxy?.animate.searchAnimate.enter"
      :leave-active-class="proxy?.animate.searchAnimate.leave">
      <div v-show="showSearch" class="mb-[10px]">
        <el-card shadow="hover">
          <el-form ref="queryFormRef" :model="queryParams" :inline="true">
            <el-form-item label="日期" prop="infoDate">
              <el-input v-model="queryParams.infoDate" placeholder="请输入日期" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="规则Id" prop="analyseNo">
              <el-input v-model="queryParams.analyseNo" placeholder="请输入规则Id" clearable @keyup.enter="handleQuery" />
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
              v-hasPermi="['live:productAnalyse:add']">刷新数据</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="success" plain icon="Refresh" @click="handleUpdate()"
              v-hasPermi="['live:productAnalyse:edit']">验证结果</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="danger" plain icon="Delete" :disabled="multiple" @click="handleDelete()"
              v-hasPermi="['live:productAnalyse:remove']">删除</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="warning" plain icon="Download" @click="handleExport"
              v-hasPermi="['live:productAnalyse:export']">导出</el-button>
          </el-col>
          <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
        </el-row>
      </template>

      <el-table v-loading="loading" :data="productAnalyseList" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="主键" align="center" prop="id" v-if="false" />
        <el-table-column label="规则" align="center" prop="analyseNo" width="80" />
        <el-table-column label="日期" align="center" prop="infoDate" width="100" />
        <el-table-column label="统计结果" prop="analyseJson" :show-overflow-tooltip="true">
          <template #default="scope">
            <span>{{scope.row.analyseList.length}}</span>
            <div @click="copyToClipboard(scope.row.analyseList,proxy)">{{scope.row.analyseList}}</div>
          </template>
        </el-table-column>
        <el-table-column label="验证结果" prop="verifyJson" width="300" :show-overflow-tooltip="true">
          <template #default="scope">
            <span>{{scope.row.verifyList.length}}</span>
            <div @click="copyToClipboard(scope.row.verifyList,proxy)">{{scope.row.verifyList}}</div>
          </template>
        </el-table-column>
        <!-- <el-table-column label="统计结果" align="center" prop="analyseJson" /> -->
        <!-- <el-table-column label="验证结果" align="center" prop="verifyJson" /> -->
        <el-table-column label="正确率" align="center" prop="accuracy" width="88" />
        <el-table-column label="更新时间" align="center" prop="updateTime" width="180">
          <template #default="scope">
            <span>{{ parseTime(scope.row.updateTime) }}</span>
          </template>
        </el-table-column>
      </el-table>

      <pagination v-show="total > 0" :total="total" v-model:page="queryParams.pageNum"
        v-model:limit="queryParams.pageSize" @pagination="getList" />
    </el-card>
  </div>
</template>

<script setup name="ProductAnalyse" lang="ts">
  import { listProductAnalyse, getProductAnalyse, delProductAnalyse, addProductAnalyse, updateProductAnalyse } from '@/api/live/productAnalyse';
  import { ProductAnalyseVO, ProductAnalyseQuery, ProductAnalyseForm } from '@/api/live/productAnalyse/types';

  const { proxy } = getCurrentInstance() as ComponentInternalInstance;

  const productAnalyseList = ref<ProductAnalyseVO[]>([]);
  const buttonLoading = ref(false);
  const loading = ref(true);
  const showSearch = ref(true);
  const ids = ref<Array<string | number>>([]);
  const single = ref(true);
  const multiple = ref(true);
  const total = ref(0);

  const queryFormRef = ref<ElFormInstance>();
  const productAnalyseFormRef = ref<ElFormInstance>();

  const dialog = reactive<DialogOption>({
    visible: false,
    title: ''
  });

  const initFormData : ProductAnalyseForm = {
    id: undefined,
    infoDate: undefined,
    analyseNo: undefined,
    analyseJson: undefined,
    verifyJson: undefined,
    accuracy: undefined,
  }
  const data = reactive<PageData<ProductAnalyseForm, ProductAnalyseQuery>>({
    form: { ...initFormData },
    queryParams: {
      pageNum: 1,
      pageSize: 10,
      orderByColumn: 'id',
      isAsc: 'desc',
      infoDate: undefined,
      analyseNo: undefined,
      params: {
      }
    },
    rules: {
      id: [
        { required: true, message: "主键不能为空", trigger: "blur" }
      ],
      infoDate: [
        { required: true, message: "日期不能为空", trigger: "blur" }
      ],
      analyseNo: [
        { required: true, message: "规则Id不能为空", trigger: "blur" }
      ],
    }
  });

  const { queryParams, form, rules } = toRefs(data);

  /** 查询统计分析列表 */
  const getList = async () => {
    loading.value = true;
    const res = await listProductAnalyse(queryParams.value);
    productAnalyseList.value = res.rows;
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
    productAnalyseFormRef.value?.resetFields();
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
  const handleSelectionChange = (selection : ProductAnalyseVO[]) => {
    ids.value = selection.map(item => item.id);
    single.value = selection.length != 1;
    multiple.value = !selection.length;
  }

  /** 新增按钮操作 */
  const handleAdd = async () => {
    await proxy?.$modal.confirm('是否更新数据？').finally(() => loading.value = false);
    await addProductAnalyse().finally(() => buttonLoading.value = false);
    proxy?.$modal.msgSuccess("操作成功");
    await getList();
  }

  /** 修改按钮操作 */
  const handleUpdate = async () => {
    await proxy?.$modal.confirm('是否更新数据？').finally(() => loading.value = false);
    await updateProductAnalyse().finally(() => buttonLoading.value = false);
    proxy?.$modal.msgSuccess("操作成功");
    await getList();
  }

  /** 删除按钮操作 */
  const handleDelete = async (row ?: ProductAnalyseVO) => {
    const _ids = row?.id || ids.value;
    await proxy?.$modal.confirm('是否确认删除统计分析编号为"' + _ids + '"的数据项？').finally(() => loading.value = false);
    await delProductAnalyse(_ids);
    proxy?.$modal.msgSuccess("删除成功");
    await getList();
  }

  /** 导出按钮操作 */
  const handleExport = () => {
    proxy?.download('live/productAnalyse/export', {
      ...queryParams.value
    }, `productAnalyse_${new Date().getTime()}.xlsx`)
  }

  onMounted(() => {
    getList();
  });
</script>
